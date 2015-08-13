package com.techlooper.service.impl;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.model.Language;
import com.techlooper.repository.userimport.JobAlertRegistrationRepository;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.JobAlertService;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
import java.util.*;

import static com.techlooper.util.DateTimeUtils.parseDate2String;
import static com.techlooper.util.DateTimeUtils.parseString2Date;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

@Service
public class JobAlertServiceImpl implements JobAlertService {

    @Value("${jobAlert.period}")
    private int CONFIGURED_JOB_ALERT_PERIOD;

    @Value("${jobAlert.launchDate}")
    private String CONFIGURED_JOB_ALERT_LAUNCH_DATE;

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

    @Override
    public List<ScrapeJobEntity> searchJob(JobAlertRegistrationEntity JobAlertRegistrationEntity) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        BoolQueryBuilder queryBuilder = boolQuery();

        if (StringUtils.isNotEmpty(JobAlertRegistrationEntity.getKeyword())) {
            queryBuilder.must(queryString(JobAlertRegistrationEntity.getKeyword()));
        }

        if (StringUtils.isNotEmpty(JobAlertRegistrationEntity.getLocation())) {
            queryBuilder.must(matchQuery("location", JobAlertRegistrationEntity.getLocation()));
        }

        queryBuilder.must(rangeQuery("createdDateTime").from("now-7d/d"));
        searchQueryBuilder.withQuery(queryBuilder);
        searchQueryBuilder.withSort(fieldSort("createdDateTime").order(SortOrder.DESC));
        searchQueryBuilder.withPageable(new PageRequest(0, 10));

        return scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
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
    public void sendEmail(JobAlertRegistrationEntity jobAlertRegistrationEntity, List<ScrapeJobEntity> scrapeJobEntities) throws Exception {
        String mailSubject = jobAlertRegistrationEntity.getLang() == Language.vi ? jobAlertMailSubjectVn : jobAlertMailSubjectEn;
        Address[] recipientAddresses = InternetAddress.parse(jobAlertRegistrationEntity.getEmail());
        Template template = jobAlertRegistrationEntity.getLang() == Language.vi ? jobAlertMailTemplateVi : jobAlertMailTemplateEn;

        jobAlertMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("email", jobAlertRegistrationEntity.getEmail());
        templateModel.put("numberOfJobs", scrapeJobEntities.size());
        templateModel.put("jobs", scrapeJobEntities);

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, jobAlertRegistrationEntity.getKeyword(), jobAlertRegistrationEntity.getLocation());
        jobAlertMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        jobAlertMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        jobAlertMailMessage.saveChanges();
        mailSender.send(jobAlertMailMessage);


        jobAlertRegistrationEntity.setLastEmailSentDateTime(parseDate2String(new Date(), "dd/MM/yyyy HH:mm"));
        jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
    }

    private int getJobAlertBucketNumber(int period) throws Exception {
        Date currentDate = new Date();
        Date launchDate = parseString2Date(CONFIGURED_JOB_ALERT_LAUNCH_DATE, "dd/MM/yyyy");
        int numberOfDays = Days.daysBetween(new DateTime(launchDate), new DateTime(currentDate)).getDays();
        return numberOfDays % period;
    }

}
