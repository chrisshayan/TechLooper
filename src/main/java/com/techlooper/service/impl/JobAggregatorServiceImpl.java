package com.techlooper.service.impl;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.JobEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.*;
import com.techlooper.repository.userimport.JobAlertRegistrationRepository;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.EmailService;
import com.techlooper.service.ForumService;
import com.techlooper.service.JobAggregatorService;
import com.techlooper.service.JobSearchService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.DateTimeFormat;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.*;

import static com.techlooper.util.DateTimeUtils.*;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.MatchQueryBuilder.Operator;
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
    private ForumService forumService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private MimeMessage jobAlertMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private EmailService emailService;

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
        searchQueryBuilder.withQuery(termQuery("bucket", bucketNumber));

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
    public void sendEmail(JobAlertRegistrationEntity jobAlertRegistrationEntity, JobSearchResponse jobSearchResponse) {
        String recipientAddress = jobAlertRegistrationEntity.getEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.JOB_ALERT.name())
                .withLanguage(jobAlertRegistrationEntity.getLang())
                .withTemplateModel(buildJobAlertEmailTemplateModel(jobAlertRegistrationEntity, jobSearchResponse))
                .withMailMessage(jobAlertMailMessage)
                .withRecipientAddresses(recipientAddress).build();
        emailService.sendMail(emailRequestModel);
    }

    private Map<String, Object> buildJobAlertEmailTemplateModel(JobAlertRegistrationEntity jobAlertRegistrationEntity,
                                                                JobSearchResponse jobSearchResponse) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("email", jobAlertRegistrationEntity.getEmail());
        templateModel.put("numberOfJobs", jobSearchResponse.getTotalJob());
        templateModel.put("keyword", jobAlertRegistrationEntity.getKeyword());
        templateModel.put("location", jobAlertRegistrationEntity.getLocation());
        templateModel.put("locationId", jobAlertRegistrationEntity.getLocationId());

        mapJobCrawlSource(jobSearchResponse);
        templateModel.put("jobs", jobSearchResponse.getJobs());
        templateModel.put("jobAlertRegistrationId", String.valueOf(jobAlertRegistrationEntity.getJobAlertRegistrationId()));

        TopicList latestTopicList = forumService.getLatestTopics();
        List<Topic> topics = null;
        if (latestTopicList.getTopics() != null) {
            topics = latestTopicList.getTopics().stream().limit(3).collect(toList());
        }
        templateModel.put("topics", topics);
        return templateModel;
    }

    @Override
    public JobSearchResponse findJob(JobSearchCriteria criteria) {
        NativeSearchQueryBuilder searchQueryBuilder = getJobListingQueryBuilder(criteria);
        FacetedPage<ScrapeJobEntity> searchResult = scrapeJobRepository.search(searchQueryBuilder.build());

        Long totalJob = searchResult.getTotalElements();
        Integer totalPage = searchResult.getTotalPages();

        List<ScrapeJobEntity> jobs = scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
        List<JobResponse> result = getJobResponses(jobs);

        JobSearchResponse jobSearchResponse = new JobSearchResponse.Builder()
                .withTotalJob(totalJob).withTotalPage(totalPage).withPage(criteria.getPage()).withJobs(result).build();
        return jobSearchResponse;
    }

    @Override
    public void updateJobExpiration(JobSearchCriteria criteria) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        searchQueryBuilder.withQuery(termQuery("crawlSource", criteria.getCrawlSource()));

        int page = 0;
        List<ScrapeJobEntity> jobs = new ArrayList<>();
        do {
            searchQueryBuilder.withPageable(new PageRequest(page, 100));
            FacetedPage<ScrapeJobEntity> searchResult = scrapeJobRepository.search(searchQueryBuilder.build());
            jobs = searchResult.getContent();

            if (!jobs.isEmpty()) {
                for (ScrapeJobEntity job : jobs) {
                    JobEntity jobEntity = vietnamWorksJobSearchService.findJobById(job.getJobId());
                    if (jobEntity != null) {
                        job.setIsActive(jobEntity.getIsActive());

                        DateTime parsedTime = ISODateTimeFormat.dateOptionalTimeParser().parseDateTime(jobEntity.getApprovedDate());
                        DateTimeFormatter basicDateTimeFmt = DateTimeFormat.forPattern(BASIC_DATE_PATTERN);
                        job.setCreatedDateTime(parsedTime.toString(basicDateTimeFmt));
                        scrapeJobRepository.save(job);
                    }
                }
            }
            page++;
        } while (!jobs.isEmpty());
    }

    @Override
    public boolean exceedJobAlertRegistrationLimit(String email) {
        final long CONFIGURED_JOB_ALERT_LIMIT_REGISTRATION = 5;
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("jobAlertRegistration");

        if (StringUtils.isNotEmpty(email)) {
            searchQueryBuilder.withQuery(matchPhraseQuery("email", email));
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
        Boolean isTopPriorityJob = (jobType == JobTypeEnum.TOP_PRIORITY) ? Boolean.TRUE : null;

        VNWJobSearchResponse vnwJobSearchResponse;
        int sum = 0;
        do {
            vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(jobSearchRequest);
            if (vnwJobSearchResponse.hasData()) {
                List<ScrapeJobEntity> jobEntities = vnwJobSearchResponse.getData().getJobs().stream().map(job ->
                        convertToJobEntity(job, isTopPriorityJob)).collect(toList());
                scrapeJobRepository.save(jobEntities);
                jobSearchRequest.setPageNumber(jobSearchRequest.getPageNumber() + 1);
                jobSearchRequestBuilder.withPageNumber(jobSearchRequest.getPageNumber());
                sum += vnwJobSearchResponse.getData().getTotal();
            }
        } while (vnwJobSearchResponse.hasData());

        return sum;
    }

    @Override
    public JobSearchCriteria findJobAlertCriteriaById(Long jobAlertRegistrationId) {
        JobAlertRegistrationEntity registrationEntity = jobAlertRegistrationRepository.findOne(jobAlertRegistrationId);

        JobSearchCriteria criteria = new JobSearchCriteria();
        if (registrationEntity != null) {
            criteria.setKeyword(registrationEntity.getKeyword());
            criteria.setLocation(registrationEntity.getLocation());
            criteria.setLocationId(registrationEntity.getLocationId());
        }
        return criteria;
    }

    @Override
    public JobAlertRegistrationEntity getJobAlertRegistrationById(Long id) {
        return jobAlertRegistrationRepository.findOne(id);
    }

    private ScrapeJobEntity convertToJobEntity(VNWJobSearchResponseDataItem job, Boolean isTopPriority) {
        ScrapeJobEntity jobEntity = dozerMapper.map(job, ScrapeJobEntity.class);
        jobEntity.setTopPriority(isTopPriority);
        jobEntity.setCrawlSource("vietnamworks");
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
                ((BoolQueryBuilder) queryBuilder).must(
                        multiMatchQuery(criteria.getKeyword()).field("jobTitle", 8).field("company", 2));
            }
            if (StringUtils.isNotEmpty(criteria.getLocation())) {
                ((BoolQueryBuilder) queryBuilder).must(matchQuery("location", criteria.getLocation()).operator(Operator.AND));
            }
        }

        BoolFilterBuilder filterBuilder = new BoolFilterBuilder();
        filterBuilder.mustNot(termFilter("isActive", 0));
        filterBuilder.must(rangeFilter("createdDateTime").from("now-30d/d"));
        if (criteria.getTopPriority() != null) {
            if (criteria.getTopPriority()) {
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
        int numberOfDays = daysBetween(launchDate, new Date());
        return numberOfDays % period.getValue();
    }

    private List<JobResponse> getJobResponses(List<ScrapeJobEntity> jobs) {
        List<JobResponse> result = new ArrayList<>();
        if (!jobs.isEmpty()) {
            for (ScrapeJobEntity jobEntity : jobs) {
                JobResponse job = dozerMapper.map(jobEntity, JobResponse.class);
                job.setTopPriority(jobEntity.getTopPriority() != null ? jobEntity.getTopPriority() : Boolean.FALSE);
                result.add(job);
            }
        }
        return result;
    }

    private void mapJobCrawlSource(JobSearchResponse jobSearchResponse) {
        for (JobResponse job : jobSearchResponse.getJobs()) {
            if (StringUtils.isNotEmpty(job.getCrawlSource())) {
                String crawlSource = job.getCrawlSource();
                StringTokenizer tokenizer = new StringTokenizer(crawlSource, "-");
                if (tokenizer.hasMoreTokens()) {
                    String sourceName = tokenizer.nextToken();
                    job.setCrawlSource(sourceName.toUpperCase());
                }
            }
        }

    }

}
