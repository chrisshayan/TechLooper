package com.techlooper.service.impl;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.*;
import com.techlooper.repository.userimport.JobAlertRegistrationRepository;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.JobAggregatorService;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.DateTimeUtils;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.StringWriter;
import java.util.*;

import static com.techlooper.util.DateTimeUtils.*;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.MatchQueryBuilder.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class JobAggregatorServiceImpl implements JobAggregatorService {

    @Value("${jobAlert.launchDate}")
    private String CONFIGURED_JOB_ALERT_LAUNCH_DATE;

    @Resource
    private ScrapeJobRepository scrapeJobRepository;

    @Resource
    private JobAlertRegistrationRepository jobAlertRegistrationRepository;

    @Resource
    private JobSearchService vietnamWorksJobSearchService;

    @Resource
    private Mapper dozerMapper;

    @Value("${jobAlert.subject.en}")
    private String jobAlertMailSubjectEn;

    @Value("${jobAlert.subject.vi}")
    private String jobAlertMailSubjectVn;

    @Resource
    private Template jobAlertMailTemplateEn;

    @Resource
    private Template jobAlertMailTemplateVi;

    @Resource
    private MimeMessage jobAlertMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JavaMailSender mailSender;

    @Override
    public JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) throws Exception {
        JobAlertRegistrationEntity jobAlertRegistrationEntity =
                dozerMapper.map(jobAlertRegistration, JobAlertRegistrationEntity.class);
        jobAlertRegistrationEntity.setJobAlertRegistrationId(new Date().getTime());
        jobAlertRegistrationEntity.setCreatedDate(currentDate(BASIC_DATE_PATTERN));
        jobAlertRegistrationEntity.setBucket(getJobAlertBucketNumber(JobAlertPeriodEnum.WEEKLY));
        return jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
    }

    @Override
    public List<JobAlertRegistrationEntity> findJobAlertRegistration(JobAlertPeriodEnum period) throws Exception {
        List<JobAlertRegistrationEntity> jobAlertRegistrationEntities = new ArrayList<>();
        int bucketNumber = getJobAlertBucketNumber(period);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("jobAlertRegistration");
        searchQueryBuilder.withQuery(QueryBuilders.termQuery("bucket", bucketNumber));

        int totalPages = jobAlertRegistrationRepository.search(searchQueryBuilder.build()).getTotalPages();
        int pageIndex = 0;
        while (pageIndex < totalPages) {
            searchQueryBuilder.withPageable(new PageRequest(pageIndex, 50));
            List<JobAlertRegistrationEntity> result = jobAlertRegistrationRepository.search(searchQueryBuilder.build()).getContent();
            jobAlertRegistrationEntities.addAll(result);
            pageIndex++;
        }

        return jobAlertRegistrationEntities;
    }

    @Override
    public void sendEmail(JobAlertRegistrationEntity jobAlertRegistrationEntity, JobSearchResponse jobSearchResponse) throws Exception {
        String mailSubject = jobAlertRegistrationEntity.getLang() == Language.vi ? jobAlertMailSubjectVn : jobAlertMailSubjectEn;
        Address[] recipientAddresses = InternetAddress.parse(jobAlertRegistrationEntity.getEmail());
        Template template = jobAlertRegistrationEntity.getLang() == Language.vi ? jobAlertMailTemplateVi : jobAlertMailTemplateEn;

        jobAlertMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("email", jobAlertRegistrationEntity.getEmail());
        templateModel.put("numberOfJobs", jobSearchResponse.getTotalJob());
        templateModel.put("keyword", jobAlertRegistrationEntity.getKeyword());
        templateModel.put("location", jobAlertRegistrationEntity.getLocation());
        templateModel.put("locationId", jobAlertRegistrationEntity.getLocationId());
        templateModel.put("jobs", jobSearchResponse.getJobs());
        templateModel.put("searchPath", buildSearchPath(jobAlertRegistrationEntity));

        template.process(templateModel, stringWriter);
        jobAlertMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        jobAlertMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        jobAlertMailMessage.saveChanges();
        mailSender.send(jobAlertMailMessage);

        jobAlertRegistrationEntity.setLastEmailSentDateTime(currentDate(BASIC_DATE_TIME_PATTERN));
        jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
    }

    @Override
    public JobSearchResponse findJob(JobSearchCriteria criteria) {
        NativeSearchQueryBuilder searchQueryBuilder = getJobListingQueryBuilder(criteria);
        FacetedPage<ScrapeJobEntity> searchResult = scrapeJobRepository.search(searchQueryBuilder.build());

        Long totalJob = searchResult.getTotalElements();
        Integer totalPage = searchResult.getTotalPages();

        List<ScrapeJobEntity> jobs = scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
        List<JobResponse> result = new ArrayList<>();
        if (!jobs.isEmpty()) {
            for (ScrapeJobEntity jobEntity : jobs) {
                JobResponse job = dozerMapper.map(jobEntity, JobResponse.class);
                job.setTopPriority(jobEntity.getTopPriority() != null ? jobEntity.getTopPriority() : Boolean.FALSE);
                result.add(job);
            }
        }

        JobSearchResponse jobSearchResponse = new JobSearchResponse.Builder()
                .withTotalJob(totalJob).withTotalPage(totalPage).withPage(criteria.getPage()).withJobs(result).build();
        return jobSearchResponse;
    }

    @Override
    public boolean exceedJobAlertRegistrationLimit(String email) {
        final long CONFIGURED_JOB_ALERT_LIMIT_REGISTRATION = 5;
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("jobAlertRegistration");

        if (StringUtils.isNotEmpty(email)) {
            searchQueryBuilder.withQuery(QueryBuilders.matchPhraseQuery("email", email));
        }

        long numberOfRegistrations = jobAlertRegistrationRepository.search(searchQueryBuilder.build()).getTotalElements();
        return numberOfRegistrations >= CONFIGURED_JOB_ALERT_LIMIT_REGISTRATION;
    }

    @Override
    public void updateSendEmailResultCode(JobAlertRegistrationEntity jobAlertRegistrationEntity, JobAlertEmailResultEnum result) {
        if (jobAlertRegistrationEntity != null) {
            jobAlertRegistrationEntity.setLastEmailSentCode(result.getValue());
            jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
        }
    }

    @Override
    public int importVietnamworksJob(JobTypeEnum jobType) {
        final String JOB_CATEGORY_IT = "35,55,57";
        VNWJobSearchRequest.Builder jobSearchRequestBuilder = new VNWJobSearchRequest.Builder()
                .withJobCategories(JOB_CATEGORY_IT).withTechlooperJobType(jobType.getValue()).withPageNumber(1).withPageSize(20);
        VNWJobSearchRequest jobSearchRequest = jobSearchRequestBuilder.build();
        Boolean isTopPriorityJob = (jobType == JobTypeEnum.TOP_PRIORITY) ? Boolean.TRUE : Boolean.FALSE;

        VNWJobSearchResponse vnwJobSearchResponse;
        do {
            vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(jobSearchRequest);
            if (vnwJobSearchResponse.hasData()) {
                List<ScrapeJobEntity> jobEntities = vnwJobSearchResponse.getData().getJobs().stream().map(job ->
                        convertToJobEntity(job, isTopPriorityJob)).collect(toList());
                scrapeJobRepository.save(jobEntities);
                jobSearchRequest.setPageNumber(jobSearchRequest.getPageNumber() + 1);
                jobSearchRequestBuilder.withPageNumber(jobSearchRequest.getPageNumber());
            }
        } while (vnwJobSearchResponse.hasData());

        return vnwJobSearchResponse != null ? vnwJobSearchResponse.getData().getTotal() : 0;
    }

    private String buildSearchPath(JobAlertRegistrationEntity jobAlertRegistrationEntity) {
        StringBuilder pathBuilder = new StringBuilder("");
        if (StringUtils.isNotEmpty(jobAlertRegistrationEntity.getKeyword())) {
            pathBuilder.append(jobAlertRegistrationEntity.getKeyword().replaceAll("\\W", "-"));
        }

        if (jobAlertRegistrationEntity.getLocationId() != null) {
            pathBuilder.append("+");
            pathBuilder.append(jobAlertRegistrationEntity.getLocationId());
        }

        if (StringUtils.isNotEmpty(jobAlertRegistrationEntity.getLocation())) {
            pathBuilder.append("+");
            pathBuilder.append(jobAlertRegistrationEntity.getLocation().replaceAll("\\W", "-"));
        }

        return pathBuilder.toString();
    }

    private ScrapeJobEntity convertToJobEntity(VNWJobSearchResponseDataItem job, Boolean isTopPriority) {
        ScrapeJobEntity jobEntity = dozerMapper.map(job, ScrapeJobEntity.class);
        jobEntity.setTopPriority(isTopPriority);
        jobEntity.setCrawlSource("vietnamworks");
        if (!scrapeJobRepository.exists(String.valueOf(job.getJobId()))) {
            jobEntity.setCreatedDateTime(currentDate(BASIC_DATE_PATTERN));
        }
        return jobEntity;
    }

    private NativeSearchQueryBuilder getJobListingQueryBuilder(JobSearchCriteria criteria) {
        final int NUMBER_OF_ITEMS_PER_PAGE = 10;
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");

        QueryBuilder queryBuilder = null;
        if (StringUtils.isEmpty(criteria.getKeyword()) && StringUtils.isEmpty(criteria.getLocation())) {
            queryBuilder = matchAllQuery();
        } else {
            queryBuilder = boolQuery();
            if (StringUtils.isNotEmpty(criteria.getKeyword())) {
                ((BoolQueryBuilder) queryBuilder).must(multiMatchQuery(criteria.getKeyword(), "jobTitle", "company"));
            }
            if (StringUtils.isNotEmpty(criteria.getLocation())) {
                ((BoolQueryBuilder) queryBuilder).must(matchQuery("location", criteria.getLocation()).operator(Operator.AND));
            }
        }

        BoolFilterBuilder filterBuilder = new BoolFilterBuilder();
        filterBuilder.must(rangeFilter("createdDateTime").from("now-30d/d"));
        if (criteria.getTopPriority() != null) {
            if (criteria.getTopPriority() == true) {
                filterBuilder.must(termFilter("topPriority", true));
            } else {
                filterBuilder.mustNot(termFilter("topPriority", true));
            }
        }

        searchQueryBuilder.withQuery(filteredQuery(queryBuilder, filterBuilder));
        searchQueryBuilder.withSort(SortBuilders.fieldSort("topPriority").order(SortOrder.DESC));
        searchQueryBuilder.withSort(SortBuilders.scoreSort());
        searchQueryBuilder.withSort(SortBuilders.fieldSort("createdDateTime").order(SortOrder.DESC));
        searchQueryBuilder.withPageable(new PageRequest(criteria.getPage() - 1, NUMBER_OF_ITEMS_PER_PAGE));
        return searchQueryBuilder;
    }

    private int getJobAlertBucketNumber(JobAlertPeriodEnum period) throws Exception {
        Date launchDate = string2Date(CONFIGURED_JOB_ALERT_LAUNCH_DATE, BASIC_DATE_PATTERN);
        int numberOfDays = DateTimeUtils.daysBetween(launchDate, new Date());
        return numberOfDays % period.getValue();
    }
}
