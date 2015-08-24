package com.techlooper.service.impl;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.*;
import com.techlooper.repository.userimport.JobAlertRegistrationRepository;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.JobAlertService;
import com.techlooper.service.JobSearchService;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.techlooper.util.DateTimeUtils.parseDate2String;
import static com.techlooper.util.DateTimeUtils.parseString2Date;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class JobAlertServiceImpl implements JobAlertService {

    public final static int NUMBER_OF_ITEMS_PER_PAGE = 10;

    private static final long CONFIGURED_JOB_ALERT_LIMIT_REGISTRATION = 5;

    @Value("${jobAlert.period}")
    private int CONFIGURED_JOB_ALERT_PERIOD;

    @Value("${jobAlert.launchDate}")
    private String CONFIGURED_JOB_ALERT_LAUNCH_DATE;

    @Value("${vnw.api.configuration.category.it.software.en}")
    private String JOB_CATEGORY_IT_SOFTWARE;

    @Resource
    private ScrapeJobRepository scrapeJobRepository;

    @Resource
    private JobAlertRegistrationRepository jobAlertRegistrationRepository;

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

    @Resource
    private JobSearchService vietnamWorksJobSearchService;

    @Override
    public List<ScrapeJobEntity> searchJob(JobAlertRegistrationEntity jobAlertRegistrationEntity) {
        NativeSearchQueryBuilder searchQueryBuilder = getJobAlertSearchQueryBuilder(jobAlertRegistrationEntity);
        searchQueryBuilder.withPageable(new PageRequest(0, 5));
        List<ScrapeJobEntity> jobs = scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
        return jobs;
    }

    @Override
    public Long countJob(JobAlertRegistrationEntity jobAlertRegistrationEntity) {
        NativeSearchQueryBuilder searchQueryBuilder = getJobAlertSearchQueryBuilder(jobAlertRegistrationEntity);
        return scrapeJobRepository.search(searchQueryBuilder.build()).getTotalElements();
    }

    @Override
    public JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) throws Exception {
        JobAlertRegistrationEntity jobAlertRegistrationEntity =
                dozerMapper.map(jobAlertRegistration, JobAlertRegistrationEntity.class);

        Date currentDate = new Date();
        jobAlertRegistrationEntity.setJobAlertRegistrationId(currentDate.getTime());
        jobAlertRegistrationEntity.setCreatedDate(parseDate2String(currentDate, "dd/MM/yyyy"));
        jobAlertRegistrationEntity.setBucket(getJobAlertBucketNumber(CONFIGURED_JOB_ALERT_PERIOD));
        return jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
    }

    @Override
    public List<JobAlertRegistrationEntity> searchJobAlertRegistration(int period) throws Exception {
        List<JobAlertRegistrationEntity> jobAlertRegistrationEntities = new ArrayList<>();
        int bucketNumber = getJobAlertBucketNumber(CONFIGURED_JOB_ALERT_PERIOD);

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
    public void sendEmail(Long numberOfJobs, JobAlertRegistrationEntity jobAlertRegistrationEntity, List<ScrapeJobEntity> scrapeJobEntities) throws Exception {
        String mailSubject = jobAlertRegistrationEntity.getLang() == Language.vi ? jobAlertMailSubjectVn : jobAlertMailSubjectEn;
        Address[] recipientAddresses = InternetAddress.parse(jobAlertRegistrationEntity.getEmail());
        Template template = jobAlertRegistrationEntity.getLang() == Language.vi ? jobAlertMailTemplateVi : jobAlertMailTemplateEn;

        jobAlertMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("email", jobAlertRegistrationEntity.getEmail());
        templateModel.put("numberOfJobs", numberOfJobs);
        templateModel.put("keyword", jobAlertRegistrationEntity.getKeyword());
        templateModel.put("location", jobAlertRegistrationEntity.getLocation());
        templateModel.put("locationId", jobAlertRegistrationEntity.getLocationId());
        templateModel.put("jobs", scrapeJobEntities.stream().limit(NUMBER_OF_ITEMS_PER_PAGE).collect(Collectors.toList()));
        templateModel.put("searchPath", buildSearchPath(jobAlertRegistrationEntity));

        template.process(templateModel, stringWriter);
        jobAlertMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        jobAlertMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        jobAlertMailMessage.saveChanges();
        mailSender.send(jobAlertMailMessage);


        jobAlertRegistrationEntity.setLastEmailSentDateTime(parseDate2String(new Date(), "dd/MM/yyyy HH:mm"));
        jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
    }

    @Override
    public List<JobResponse> listJob(JobListingCriteria criteria) {
        List<JobResponse> result = new ArrayList<>();
        VNWJobSearchRequest vnwJobSearchRequest = getTopPriorityJobSearchRequest(criteria);
        VNWJobSearchResponse vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(vnwJobSearchRequest);
        if (vnwJobSearchResponse.hasData()) {
            Set<VNWJobSearchResponseDataItem> vnwJobs = vnwJobSearchResponse.getData().getJobs();
            result.addAll(aggregateJobs(vnwJobs));
        }

        NativeSearchQueryBuilder searchQueryBuilder = getJobListingQueryBuilder(criteria);
        List<ScrapeJobEntity> jobs = scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
        if (!jobs.isEmpty()) {
            for (ScrapeJobEntity jobEntity : jobs) {
                JobResponse.Builder builder = new JobResponse.Builder();
                JobResponse job = builder.withUrl(jobEntity.getJobTitleUrl())
                        .withTitle(jobEntity.getJobTitle())
                        .withCompany(jobEntity.getCompany())
                        .withLocation(jobEntity.getLocation())
                        .withSalary(jobEntity.getSalary())
                        .withPostedOn(jobEntity.getCreatedDateTime()).build();
                result.add(job);
            }
        }
        return result;
    }

    private List<JobResponse> aggregateJobs(Set<VNWJobSearchResponseDataItem> vnwJobs) {
        List<JobResponse> result = new ArrayList<>();
        for(VNWJobSearchResponseDataItem job : vnwJobs) {
            JobResponse.Builder builder = new JobResponse.Builder();
            if (job.getSalaryMax() != null && job.getSalaryMax() > 0) {
                JobResponse jobResponse = builder.withTitle(job.getTitle()).withUrl(job.getUrl()).withLocation(job.getLocation())
                        .withCompany(job.getCompany()).withLevel(job.getLevel()).withLogoUrl(job.getLogoUrl())
                        .withPostedOn(job.getPostedOn()).withSalaryMin(job.getSalaryMin()).withSalaryMax(job.getSalaryMax())
                        .withTechlooperJobType(1).build();
                result.add(jobResponse);
            }
        }
        return result;
    }

    private VNWJobSearchRequest getTopPriorityJobSearchRequest(JobListingCriteria criteria) {
        VNWJobSearchRequest vnwJobSearchRequest = new VNWJobSearchRequest();
        vnwJobSearchRequest.setJobTitle(criteria.getKeyword());
        vnwJobSearchRequest.setJobLocation(criteria.getLocation());
        vnwJobSearchRequest.setJobCategories(JOB_CATEGORY_IT_SOFTWARE);
        vnwJobSearchRequest.setTechlooperJobType(1);
        vnwJobSearchRequest.setPageNumber(1);
        vnwJobSearchRequest.setPageSize(20);
        return vnwJobSearchRequest;
    }

    @Override
    public Long countJob(JobListingCriteria criteria) {
        NativeSearchQueryBuilder searchQueryBuilder = getJobListingQueryBuilder(criteria);
        return scrapeJobRepository.search(searchQueryBuilder.build()).getTotalElements();
    }

    private NativeSearchQueryBuilder getJobListingQueryBuilder(JobListingCriteria criteria) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");

        if (StringUtils.isEmpty(criteria.getKeyword()) && StringUtils.isEmpty(criteria.getLocation())) {
            searchQueryBuilder.withQuery(matchAllQuery());
        } else {
            BoolQueryBuilder queryBuilder = boolQuery();

            if (StringUtils.isNotEmpty(criteria.getKeyword())) {
                queryBuilder.must(queryString(criteria.getKeyword()));
            }

            if (StringUtils.isNotEmpty(criteria.getLocation())) {
                queryBuilder.must(matchQuery("location", criteria.getLocation()));
            }

            searchQueryBuilder.withQuery(queryBuilder);
        }

        searchQueryBuilder.withSort(SortBuilders.fieldSort("createdDateTime").order(SortOrder.DESC));
        searchQueryBuilder.withPageable(new PageRequest(criteria.getPage() - 1, NUMBER_OF_ITEMS_PER_PAGE));
        return searchQueryBuilder;
    }

    private int getJobAlertBucketNumber(int period) throws Exception {
        Date currentDate = new Date();
        Date launchDate = parseString2Date(CONFIGURED_JOB_ALERT_LAUNCH_DATE, "dd/MM/yyyy");
        int numberOfDays = Days.daysBetween(new DateTime(launchDate), new DateTime(currentDate)).getDays();
        return numberOfDays % period;
    }

//    private List<ScrapeJobEntity> sortJobByDescendingStartDate(List<ScrapeJobEntity> jobs) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        return jobs.stream().sorted((job1, job2) -> {
//            try {
//                long challenge2StartDate = sdf.parse(job2.getCreatedDateTime()).getTime();
//                long challenge1StartDate = sdf.parse(job1.getCreatedDateTime()).getTime();
//                if (challenge2StartDate - challenge1StartDate > 0) {
//                    return 1;
//                } else if (challenge2StartDate - challenge1StartDate < 0) {
//                    return -1;
//                } else {
//                    return 0;
//                }
//            } catch (ParseException e) {
//                return 0;
//            }
//        }).collect(Collectors.toList());
//    }

    public boolean checkIfUserExceedRegistrationLimit(String email) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("jobAlertRegistration");

        if (StringUtils.isNotEmpty(email)) {
            searchQueryBuilder.withQuery(QueryBuilders.matchPhraseQuery("email", email));
        }

        long numberOfRegistrations = jobAlertRegistrationRepository.search(searchQueryBuilder.build()).getTotalElements();
        return numberOfRegistrations >= CONFIGURED_JOB_ALERT_LIMIT_REGISTRATION;
    }

    private NativeSearchQueryBuilder getJobAlertSearchQueryBuilder(JobAlertRegistrationEntity jobAlertRegistrationEntity) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        BoolQueryBuilder queryBuilder = boolQuery();

        if (StringUtils.isNotEmpty(jobAlertRegistrationEntity.getKeyword())) {
            queryBuilder.must(queryString(jobAlertRegistrationEntity.getKeyword()));
        }

        if (StringUtils.isNotEmpty(jobAlertRegistrationEntity.getLocation())) {
            queryBuilder.must(matchQuery("location", jobAlertRegistrationEntity.getLocation()));
        }

        queryBuilder.must(rangeQuery("createdDateTime").from("now-7d/d"));
        searchQueryBuilder.withSort(SortBuilders.fieldSort("createdDateTime").order(SortOrder.DESC));
        searchQueryBuilder.withQuery(queryBuilder);
        return searchQueryBuilder;
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
}
