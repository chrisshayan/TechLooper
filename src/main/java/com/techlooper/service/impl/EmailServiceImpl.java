package com.techlooper.service.impl;

import com.techlooper.dto.EmailSettingDto;
import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.entity.*;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.repository.elasticsearch.EmailTemplateRepository;
import com.techlooper.service.*;
import com.techlooper.util.DataUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.*;
import static java.util.stream.Collectors.joining;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by phuonghqh on 10/1/15.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeSubmissionService challengeSubmissionService;

    @Resource
    private MimeMessage fromTechlooperMailMessage;

    @Resource
    private Template challengeEmployerMailTemplateEn;

    @Resource
    private Template challengeEmployerMailTemplateVi;

    @Resource
    private EmailTemplateRepository emailTemplateRepository;

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private MimeMessage postChallengeMailMessage;

    @Resource
    private Template postChallengeMailTemplateEn;

    @Resource
    private Template postChallengeUpdateMailTemplateEn;

    @Resource
    private Template postChallengeMailTemplateVi;

    @Value("${mail.postChallenge.subject.vn}")
    private String postChallengeMailSubjectVn;

    @Value("${mail.postChallenge.subject.en}")
    private String postChallengeMailSubjectEn;

    @Value("${mail.postChallenge.techloopies.mailSubject}")
    private String postChallengeTechloopiesMailSubject;

    @Value("${mail.postChallenge.techloopies.updateMailSubject}")
    private String postChallengeTechloopiesUpdateMailSubject;

    @Value("${mail.postChallenge.techloopies.mailList}")
    private String postChallengeTechloopiesMailList;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private Template confirmUserJoinChallengeMailTemplateEn;

    @Resource
    private Template confirmUserJoinChallengeMailTemplateVi;

    @Value("${mail.confirmUserJoinChallenge.subject.vn}")
    private String confirmUserJoinChallengeMailSubjectVn;

    @Value("${mail.confirmUserJoinChallenge.subject.en}")
    private String confirmUserJoinChallengeMailSubjectEn;

    @Value("${mail.alertEmployerChallenge.subject.vn}")
    private String alertEmployerChallengeMailSubjectVn;

    @Value("${mail.alertEmployerChallenge.subject.en}")
    private String alertEmployerChallengeMailSubjectEn;

    @Value("${mail.techlooper.reply_to}")
    private String mailTechlooperReplyTo;

    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.notifyChallengeTimelineRegistration.subject.vn}")
    private String notifyChallengeTimelineRegistrationMailSubjectVn;

    @Value("${mail.notifyChallengeTimelineRegistration.subject.en}")
    private String notifyChallengeTimelineRegistrationMailSubjectEn;

    @Value("${mail.notifyChallengeTimelineInProgress.subject.vn}")
    private String notifyChallengeTimelineInProgressMailSubjectVn;

    @Value("${mail.notifyChallengeTimelineInProgress.subject.en}")
    private String notifyChallengeTimelineInProgressMailSubjectEn;

    @Value("${mail.dailyChallengeSummary.subject.en}")
    private String dailyChallengeSummaryMailSubjectEn;

    @Value("${mail.dailyChallengeSummary.subject.vi}")
    private String dailyChallengeSummaryMailSubjectVi;

    @Value("${mail.notifyChallengePhaseClosed.subject.en}")
    private String notifyChallengePhaseClosedMailSubjectEn;

    @Value("${mail.notifyChallengePhaseClosed.subject.vi}")
    private String notifyChallengePhaseClosedMailSubjectVi;

    @Resource
    private Template notifyChallengeTimelineMailTemplateVi;

    @Resource
    private Template notifyChallengeTimelineMailTemplateEn;

    @Resource
    private Template notifyChallengeSubmissionMailTemplateVi;

    @Resource
    private Template notifyChallengeSubmissionMailTemplateEn;

    @Resource
    private Template dailyChallengeSummaryMailTemplateVi;

    @Resource
    private Template dailyChallengeSummaryMailTemplateEn;

    @Resource
    private Template notifyChallengePhaseClosedMailTemplateVi;

    @Resource
    private Template notifyChallengePhaseClosedMailTemplateEn;

    @Resource
    private CurrencyService currencyService;

    @Resource
    private EmployerService employerService;

    public boolean sendEmail(EmailContent emailContent) {
        try {
            String subject = StringUtils.isNotEmpty(emailContent.getSubject()) ? emailContent.getSubject() : "[No Subject]";
            fromTechlooperMailMessage.setSubject(MimeUtility.encodeText(subject, "UTF-8", null));

            Template template = emailContent.getLanguage() == Language.vi ? challengeEmployerMailTemplateVi :
                    challengeEmployerMailTemplateEn;

            StringWriter stringWriter = new StringWriter();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("webBaseUrl", webBaseUrl);
            templateModel.put("emailContent", emailContent.getContent());
            template.process(templateModel, stringWriter);

            fromTechlooperMailMessage.setText(stringWriter.toString(), "UTF-8", "html");
            fromTechlooperMailMessage.setRecipients(Message.RecipientType.TO, emailContent.getRecipients());
            fromTechlooperMailMessage.setReplyTo(emailContent.getReplyTo());
            fromTechlooperMailMessage.saveChanges();
            mailSender.send(fromTechlooperMailMessage);
        } catch (Exception e) {
            LOGGER.error("Can not send email", e);
            return false;
        }
        return true;
    }

    @Override
    public List<EmailTemplateDto> getAvailableEmailTemplates(Language language) {
        List<EmailTemplateDto> emailTemplateDtoList = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("emailTemplate");
        searchQueryBuilder.withQuery(boolQuery()
                .must(termQuery("isEnable", Boolean.TRUE))
                .must(termQuery("language", language)));
        List<EmailTemplateEntity> templateEntities = DataUtils.getAllEntities(emailTemplateRepository, searchQueryBuilder);

        for (EmailTemplateEntity emailTemplateEntity : templateEntities) {
            emailTemplateDtoList.add(dozerMapper.map(emailTemplateEntity, EmailTemplateDto.class));
        }
        return emailTemplateDtoList;
    }

    @Override
    public EmailTemplateDto getTemplateById(Long templateId) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findOne(templateId);
        if (emailTemplateEntity != null && emailTemplateEntity.getIsEnable()) {
            return dozerMapper.map(emailTemplateEntity, EmailTemplateDto.class);
        }
        return null;
    }

    public void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException {
        String mailSubject = challengeEntity.getLang() == Language.vi ? postChallengeMailSubjectVn : postChallengeMailSubjectEn;
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
        Template template = challengeEntity.getLang() == Language.vi ? postChallengeMailTemplateVi : postChallengeMailTemplateEn;
        sendPostChallengeEmail(challengeEntity, mailSubject, recipientAddresses, template);
    }

    public void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, Boolean isNewChallenge)
            throws MessagingException, IOException, TemplateException {
        String mailSubject = isNewChallenge ? postChallengeTechloopiesMailSubject :
                String.format(postChallengeTechloopiesUpdateMailSubject, challengeEntity.getChallengeName());
        Template mailTemplate = isNewChallenge ? postChallengeMailTemplateEn : postChallengeUpdateMailTemplateEn;
        Address[] recipientAddresses = InternetAddress.parse(postChallengeTechloopiesMailList);
        sendPostChallengeEmail(challengeEntity, mailSubject, recipientAddresses, mailTemplate);
    }

    private Address[] getRecipientAddresses(ChallengeEntity challengeEntity, boolean includeAuthor) throws AddressException {
        Set<String> emails = new HashSet<>(challengeEntity.getReceivedEmails());
        if (includeAuthor) {
            emails.add(challengeEntity.getAuthorEmail());
        }
        return InternetAddress.parse(StringUtils.join(emails, ','));
    }

    @Override
    public void sendEmailNotifyRegistrantAboutChallengeTimeline(ChallengeEntity challengeEntity,
                                                                ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase, boolean isSpecificDayNotification) throws Exception {
        String mailSubject = getNotifyRegistrantChallengeTimelineSubject(challengeRegistrantEntity, challengePhase);
        Address[] recipientAddresses = InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail());
        Template template = challengeRegistrantEntity.getLang() == Language.vi ?
                notifyChallengeTimelineMailTemplateVi : notifyChallengeTimelineMailTemplateEn;

        if (isSpecificDayNotification) {
            template = challengeRegistrantEntity.getLang() == Language.vi ?
                    notifyChallengeSubmissionMailTemplateVi : notifyChallengeSubmissionMailTemplateEn;
        }

        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("challengeEntity", challengeEntity);
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), "<br/>"));
        templateModel.put("receivedEmails", StringUtils.join(challengeEntity.getReceivedEmails(), "<br/>"));
        templateModel.put("firstPlaceReward", challengeEntity.getFirstPlaceReward() != null ? challengeEntity.getFirstPlaceReward() : 0);
        templateModel.put("secondPlaceReward", challengeEntity.getSecondPlaceReward() != null ? challengeEntity.getSecondPlaceReward() : 0);
        templateModel.put("thirdPlaceReward", challengeEntity.getThirdPlaceReward() != null ? challengeEntity.getThirdPlaceReward() : 0);
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));

        int numberOfDays = getNumberOfDaysUntilPhase(challengeEntity, challengePhase);

        templateModel.put("numberOfDays", numberOfDays);
        templateModel.put("challengePhase", challengePhase.getValue());
        templateModel.put("challengeRegistrant", challengeRegistrantEntity);

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, numberOfDays, challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    private void sendPostChallengeEmail(ChallengeEntity challengeEntity, String mailSubject,
                                        Address[] recipientAddresses, Template template) throws MessagingException, IOException, TemplateException {
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("businessRequirement", challengeEntity.getBusinessRequirement());
        templateModel.put("generalNote", challengeEntity.getGeneralNote());
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), "<br/>"));
        templateModel.put("documents", challengeEntity.getDocuments());
        templateModel.put("deliverables", challengeEntity.getDeliverables());
        templateModel.put("receivedEmails", StringUtils.join(challengeEntity.getReceivedEmails(), "<br/>"));
        templateModel.put("reviewStyle", challengeEntity.getReviewStyle());
        templateModel.put("startDate", challengeEntity.getStartDateTime());
        templateModel.put("registrationDate", challengeEntity.getRegistrationDateTime());
        templateModel.put("submissionDate", challengeEntity.getSubmissionDateTime());
        templateModel.put("qualityIdea", challengeEntity.getQualityIdea());
        templateModel.put("firstPlaceReward", challengeEntity.getFirstPlaceReward() != null ? challengeEntity.getFirstPlaceReward() : 0);
        templateModel.put("secondPlaceReward", challengeEntity.getSecondPlaceReward() != null ? challengeEntity.getSecondPlaceReward() : 0);
        templateModel.put("thirdPlaceReward", challengeEntity.getThirdPlaceReward() != null ? challengeEntity.getThirdPlaceReward() : 0);
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("authorEmail", challengeEntity.getAuthorEmail());
        templateModel.put("challengeOverview", challengeEntity.getChallengeOverview());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));

        template.process(templateModel, stringWriter);
        String formatMailSubject = String.format(mailSubject, challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(formatMailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    private int getNumberOfDaysUntilPhase(ChallengeEntity challengeEntity, ChallengePhaseEnum challengePhase) throws ParseException {
        int numberOfDays = 0;
        if (challengePhase == REGISTRATION) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getRegistrationDateTime()) + 1;
        } else if (challengePhase == IN_PROGRESS) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        } else if (challengePhase == IDEA) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getIdeaSubmissionDateTime()) + 1;
        } else if (challengePhase == UIUX) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getUxSubmissionDateTime()) + 1;
        } else if (challengePhase == PROTOTYPE) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getPrototypeSubmissionDateTime()) + 1;
        } else if (challengePhase == IN_PROGRESS) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        } else if (challengePhase == FINAL) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        }
        return numberOfDays;
    }

    @Override
    public void sendEmailNotifyEmployerWhenPhaseClosed(ChallengeEntity challengeEntity, ChallengePhaseEnum currentPhase,
                                                       ChallengePhaseEnum oldPhase) throws Exception {
        String mailSubject = challengeEntity.getLang() == Language.vi ? notifyChallengePhaseClosedMailSubjectVi :
                notifyChallengePhaseClosedMailSubjectEn;
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
        Template template = challengeEntity.getLang() == Language.vi ? notifyChallengePhaseClosedMailTemplateVi :
                notifyChallengePhaseClosedMailTemplateEn;

        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("challenge", challengeEntity);
        templateModel.put("challengeId", String.valueOf(challengeEntity.getChallengeId()));
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("oldPhase", oldPhase.getValue());

        ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
        challengeService.calculateChallengePhases(challengeDetailDto);
        templateModel.put("currentPhase", currentPhase.getValue());

        Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> numberOfRegistrantByPhase =
                challengeRegistrantService.countNumberOfRegistrantsByPhase(challengeEntity.getChallengeId());
        ChallengeRegistrantPhaseItem currentPhaseRegistrants = numberOfRegistrantByPhase.get(currentPhase);
        if (currentPhaseRegistrants != null) {
            templateModel.put("currentPhaseRegistrants", currentPhaseRegistrants.getRegistration());
        }

        template.process(templateModel, stringWriter);

        if (challengeEntity.getLang() == Language.vi) {
            mailSubject = String.format(mailSubject, oldPhase.getVi(), challengeEntity.getChallengeName());
        } else {
            mailSubject = String.format(mailSubject, oldPhase.getEn(), challengeEntity.getChallengeName());
        }
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    public void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
        Template template = challengeRegistrantEntity.getLang() == Language.vi ?
                confirmUserJoinChallengeMailTemplateVi : confirmUserJoinChallengeMailTemplateEn;
        String mailSubject = challengeRegistrantEntity.getLang() == Language.vi ?
                confirmUserJoinChallengeMailSubjectVn : confirmUserJoinChallengeMailSubjectEn;
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        Address[] emailAddress = InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail());
        sendContestApplicationEmail(template, mailSubject, emailAddress, challengeEntity, challengeRegistrantEntity, false);
    }

    private String getNotifyRegistrantChallengeTimelineSubject(
            ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase) {
        if (challengeRegistrantEntity.getLang() == Language.vi) {
            if (challengePhase == REGISTRATION) {
                return notifyChallengeTimelineRegistrationMailSubjectVn;
            } else {
                return notifyChallengeTimelineInProgressMailSubjectVn;
            }
        } else {
            if (challengePhase == REGISTRATION) {
                return notifyChallengeTimelineRegistrationMailSubjectEn;
            } else {
                return notifyChallengeTimelineInProgressMailSubjectEn;
            }
        }
    }

    private void sendContestApplicationEmail(Template template, String mailSubject, Address[] recipientAddresses,
                                             ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity, boolean hasReplyTo)
            throws MessagingException, IOException, TemplateException {
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

        if (hasReplyTo) {
            postChallengeMailMessage.setReplyTo(InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail()));
        } else {
            postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        }

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challenge", challengeEntity);
        templateModel.put("challengeRegistrant", challengeRegistrantEntity);
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), "<br/>"));
        templateModel.put("receivedEmails", StringUtils.join(challengeEntity.getReceivedEmails(), "<br/>"));
        templateModel.put("firstPlaceReward", challengeEntity.getFirstPlaceReward() != null ? challengeEntity.getFirstPlaceReward() : 0);
        templateModel.put("secondPlaceReward", challengeEntity.getSecondPlaceReward() != null ? challengeEntity.getSecondPlaceReward() : 0);
        templateModel.put("thirdPlaceReward", challengeEntity.getThirdPlaceReward() != null ? challengeEntity.getThirdPlaceReward() : 0);
        templateModel.put("challengeId", String.valueOf(challengeEntity.getChallengeId()));
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));

        template.process(templateModel, stringWriter);
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    @Override
    public void sendDailySummaryEmailToChallengeOwner(ChallengeEntity challengeEntity) throws Exception {
        String mailSubject = challengeEntity.getLang() == Language.vi ? dailyChallengeSummaryMailSubjectVi :
                dailyChallengeSummaryMailSubjectEn;
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
        Template template = challengeEntity.getLang() == Language.vi ?
                dailyChallengeSummaryMailTemplateVi : dailyChallengeSummaryMailTemplateEn;
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        Address[] replyToAddresses = InternetAddress.parse(postChallengeTechloopiesMailList);
        postChallengeMailMessage.setReplyTo(replyToAddresses);
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("currentDateTime", String.valueOf(new Date().getTime()));
        templateModel.put("yesterdayDateTime", yesterdayDate());

        Long currentDateTime = new Date().getTime();
        List<ChallengeRegistrantEntity> latestRegistrants = challengeRegistrantService.findChallengeRegistrantWithinPeriod(
                challengeEntity.getChallengeId(), currentDateTime, TimePeriodEnum.TWENTY_FOUR_HOURS);
        templateModel.put("numberOfRegistrants", latestRegistrants.size());
        templateModel.put("latestRegistrants", latestRegistrants);

        List<ChallengeSubmissionEntity> latestSubmissions = challengeSubmissionService.findChallengeSubmissionWithinPeriod(
                challengeEntity.getChallengeId(), currentDateTime, TimePeriodEnum.TWENTY_FOUR_HOURS);
        templateModel.put("numberOfSubmissions", latestSubmissions.size());
        templateModel.put("latestSubmissions", latestSubmissions);

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    @Override
    public void updateSendEmailToContestantResultCode(ChallengeRegistrantEntity challengeRegistrantEntity, EmailSentResultEnum code) {
        if (challengeRegistrantEntity != null) {
            challengeRegistrantEntity.setLastEmailSentDateTime(currentDate(BASIC_DATE_TIME_PATTERN));
            challengeRegistrantEntity.setLastEmailSentResultCode(code.getValue());
            challengeRegistrantRepository.save(challengeRegistrantEntity);
        }
    }

    @Override
    public void updateSendEmailToChallengeOwnerResultCode(ChallengeEntity challengeEntity, EmailSentResultEnum code) {
        if (challengeEntity != null) {
            challengeEntity.setLastEmailSentDateTime(currentDate(BASIC_DATE_TIME_PATTERN));
            challengeEntity.setLastEmailSentResultCode(code.getValue());
            challengeRepository.save(challengeEntity);
        }
    }

    @Override
    public boolean sendEmailToRegistrant(String challengeOwner, Long registrantId, EmailContent emailContent) {
        boolean result = false;
        final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN = 4L;
        final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI = 104L;
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
        if (challengeService.isOwnerOfChallenge(challengeOwner, registrant.getChallengeId())) {
            String csvEmails = registrant.getRegistrantEmail();

            if (emailContent.getTemplateId() == ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN
                    || emailContent.getTemplateId() == ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI) {
                csvEmails = challengeRegistrantService.findRegistrantsByChallengeId(registrant.getChallengeId()).stream()
                        .map(challengeRegistrant -> challengeRegistrant.getRegistrantEmail()).collect(Collectors.joining(","));
            }

            ChallengeEntity challenge = challengeService.findChallengeById(registrant.getChallengeId(), null);
            try {
                bindEmailTemplateVariables(emailContent, challenge, registrant);
                emailContent.setRecipients(InternetAddress.parse(csvEmails));
                result = sendEmail(emailContent);
            } catch (AddressException e) {
                LOGGER.debug("Can not parse email address", e);
            }
        }
        return result;
    }

    private void bindEmailTemplateVariables(EmailContent emailContent, ChallengeEntity challenge, ChallengeRegistrantEntity registrant) {
        String subject = emailContent.getSubject();
        String body = emailContent.getContent();
        EmailSettingDto emailSettingDto = employerService.findEmployerEmailSetting(challenge.getAuthorEmail());
        // Process email subject
        subject = processEmailVariables(challenge, registrant, emailSettingDto, subject);
        // Process email body
        body = processEmailVariables(challenge, registrant, emailSettingDto, body);

        emailContent.setSubject(subject);
        emailContent.setContent(body);
        try {
            emailContent.setReplyTo(InternetAddress.parse(emailSettingDto.getReplyEmail()));
        } catch (AddressException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private String processEmailVariables(ChallengeEntity challenge, ChallengeRegistrantEntity registrant,
                                         EmailSettingDto emailSettingDto, String replacementCandidate) {
        String result = replacementCandidate;

        if (result.contains(EmailService.VAR_CONTEST_NAME)) {
            result = result.replace(EmailService.VAR_CONTEST_NAME, challenge.getChallengeName());
        }
        if (result.contains(EmailService.VAR_CONTEST_FIRST_PRIZE)) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challenge.getFirstPlaceReward()), Locale.US);
            result = result.replace(EmailService.VAR_CONTEST_FIRST_PRIZE, formatPrize);
        }
        if (result.contains(EmailService.VAR_CONTESTANT_FIRST_NAME)) {
            result = result.replace(EmailService.VAR_CONTESTANT_FIRST_NAME, registrant.getRegistrantFirstName());
        }
        if (result.contains(EmailService.VAR_MAIL_SIGNATURE)) {
            result = result.replace(EmailService.VAR_MAIL_SIGNATURE, emailSettingDto.getEmailSignature());
        }
        if (result.contains(EmailService.VAR_CONTEST_SECOND_PRIZE) && challenge.getSecondPlaceReward() != null) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challenge.getSecondPlaceReward()), Locale.US);
            formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
            result = result.replace(EmailService.VAR_CONTEST_SECOND_PRIZE, formatPrize);
        }
        if (result.contains(EmailService.VAR_CONTEST_THIRD_PRIZE) && challenge.getThirdPlaceReward() != null) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challenge.getThirdPlaceReward()), Locale.US);
            formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
            result = result.replace(EmailService.VAR_CONTEST_THIRD_PRIZE, formatPrize);
        }

        Set<ChallengeRegistrantDto> winners = challengeRegistrantService.findWinnerRegistrantsByChallengeId(challenge.getChallengeId());
        Optional<ChallengeRegistrantDto> firstWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.FIRST_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> secondWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.SECOND_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> thirdWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.THIRD_PLACE).findFirst();

        if (result.contains(EmailService.VAR_FIRST_WINNER_FIRST_NAME)
                || result.contains(EmailService.VAR_FIRST_WINNER_FULL_NAME)) {
            if (firstWinner.isPresent()) {
                String firstName = firstWinner.get().getRegistrantFirstName();
                String lastName = firstWinner.get().getRegistrantLastName();
                result = result.replace(EmailService.VAR_FIRST_WINNER_FIRST_NAME, firstName);
                result = result.replace(EmailService.VAR_FIRST_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        if (result.contains(EmailService.VAR_SECOND_WINNER_FIRST_NAME)
                || result.contains(EmailService.VAR_SECOND_WINNER_FULL_NAME)) {
            if (secondWinner.isPresent()) {
                String firstName = secondWinner.get().getRegistrantFirstName();
                String lastName = secondWinner.get().getRegistrantLastName();
                result = result.replace(EmailService.VAR_SECOND_WINNER_FIRST_NAME, firstName);
                result = result.replace(EmailService.VAR_SECOND_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        if (result.contains(EmailService.VAR_THIRD_WINNER_FIRST_NAME)
                || result.contains(EmailService.VAR_THIRD_WINNER_FULL_NAME)) {
            if (thirdWinner.isPresent()) {
                String firstName = thirdWinner.get().getRegistrantFirstName();
                String lastName = thirdWinner.get().getRegistrantLastName();
                result = result.replace(EmailService.VAR_THIRD_WINNER_FIRST_NAME, firstName);
                result = result.replace(EmailService.VAR_THIRD_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        return result;
    }

    public boolean sendEmailToDailyChallengeRegistrants(String challengeOwner, Long challengeId, Long now, EmailContent emailContent) {
        if (challengeService.isOwnerOfChallenge(challengeOwner, challengeId)) {
            List<ChallengeRegistrantEntity> registrants = challengeRegistrantService.findChallengeRegistrantWithinPeriod(challengeId, now, TimePeriodEnum.TWENTY_FOUR_HOURS);
            String csvEmails = registrants.stream().map(ChallengeRegistrantEntity::getRegistrantEmail).distinct().collect(joining(","));
            try {
                emailContent.setRecipients(InternetAddress.parse(csvEmails));
            } catch (AddressException e) {
                LOGGER.debug("Can not parse email address", e);
                return false;
            }
        }
        return sendEmail(emailContent);
    }

}
