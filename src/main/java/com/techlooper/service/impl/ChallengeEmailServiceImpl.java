package com.techlooper.service.impl;

import com.techlooper.dto.EmailSettingDto;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.*;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.*;
import static java.util.stream.Collectors.joining;

@Service
public class ChallengeEmailServiceImpl implements ChallengeEmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(ChallengeEmailServiceImpl.class);

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeSubmissionService challengeSubmissionService;

    @Resource
    private CurrencyService currencyService;

    @Resource
    private EmployerService employerService;

    @Resource
    private EmailService emailService;

    @Resource
    private MimeMessage fromTechlooperMailMessage;

    @Resource
    private MimeMessage postChallengeMailMessage;

    @Resource
    private Mapper dozerMapper;

    @Value("${mail.techlooper.mailingList}")
    private String techlooperMailingList;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Override
    public boolean sendFeedbackEmail(EmailContent emailContent) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("emailContent", emailContent.getContent());

        String recipientAddresses = emailContent.getRecipients();
        String subject = StringUtils.isNotEmpty(emailContent.getSubject()) ? emailContent.getSubject() : "[No Subject]";

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_EMPLOYER_FEEDBACK.name())
                .withLanguage(emailContent.getLanguage())
                .withSubject(subject)
                .withTemplateModel(templateModel)
                .withMailMessage(fromTechlooperMailMessage)
                .withRecipientAddresses(recipientAddresses).build();
        emailService.sendMail(emailRequestModel);
        return true;
    }

    @Override
    public void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity) {
        String recipientAddresses = getRecipientAddresses(challengeEntity, true);
        List<String> subjectVariableValues = Arrays.asList(challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_CONFIRM_ON_POST_CHALLENGE.name())
                .withLanguage(challengeEntity.getLang())
                .withTemplateModel(buildPostChallengeEmailTemplateModel(challengeEntity))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(recipientAddresses)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendEmailNotifyEmployerUpdateCriteria(ChallengeEntity challengeEntity) {
        String recipientAddresses = challengeEntity.getAuthorEmail();
        List<String> subjectVariableValues = Arrays.asList(challengeEntity.getChallengeName());

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_NOTIFY_EMPLOYER_UPDATE_CRITERIA.name())
                .withLanguage(challengeEntity.getLang())
                .withTemplateModel(buildNotifyEmployerUpdateCriteriaEmailTemplateModel(challengeEntity))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(recipientAddresses)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, Boolean isNewChallenge) {
        List<String> subjectVariableValues = Arrays.asList(challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());
        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_CONFIRM_ON_POST_CHALLENGE.name())
                .withLanguage(challengeEntity.getLang())
                .withTemplateModel(buildPostChallengeEmailTemplateModel(challengeEntity))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(techlooperMailingList)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendEmailNotifyRegistrantAboutChallengeTimeline(ChallengeEntity challengeEntity,
                                                                ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase, boolean isSpecificDayNotification) throws Exception {
        int numberOfDays = getNumberOfDaysUntilPhase(challengeEntity, challengePhase);
        List<String> subjectVariableValues = Arrays.asList(String.valueOf(numberOfDays), challengeEntity.getChallengeName());
        String recipientAddress = challengeRegistrantEntity.getRegistrantEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_NOTIFY_REGISTRANT_CHALLENGE_TIME_LINE.name())
                .withLanguage(challengeRegistrantEntity.getLang())
                .withTemplateModel(buildNotifyChallengeTimelineEmailTemplateModel(challengeEntity, challengeRegistrantEntity, challengePhase, numberOfDays))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(recipientAddress)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendEmailNotifyEmployerWhenPhaseClosed(ChallengeEntity challengeEntity,
                                                       ChallengePhaseEnum currentPhase, ChallengePhaseEnum oldPhase) {
        List<String> subjectVariableValues = Arrays.asList(oldPhase.getEn(), challengeEntity.getChallengeName());
        if (challengeEntity.getLang() == Language.vi) {
            subjectVariableValues = Arrays.asList(oldPhase.getVi(), challengeEntity.getChallengeName());
        }
        String recipientAddresses = getRecipientAddresses(challengeEntity, true);

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_NOTIFY_PHASE_JUST_CLOSED.name())
                .withLanguage(challengeEntity.getLang())
                .withTemplateModel(buildNotifyChallengePhaseClosedEmailTemplateModel(challengeEntity, oldPhase, currentPhase))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(recipientAddresses)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) {
        List<String> subjectVariableValues = Arrays.asList(challengeEntity.getChallengeName());
        String recipientAddresses = challengeRegistrantEntity.getRegistrantEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_CONFIRM_USER_ON_JOIN_CHALLENGE.name())
                .withLanguage(challengeRegistrantEntity.getLang())
                .withTemplateModel(buildConfirmUserJoinChallengeEmailTemplateModel(challengeEntity, challengeRegistrantEntity))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(recipientAddresses)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendDailySummaryEmailToChallengeOwner(ChallengeEntity challengeEntity) {
        List<String> subjectVariableValues = Arrays.asList(challengeEntity.getChallengeName());
        String recipientAddress = getRecipientAddresses(challengeEntity, true);

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_DAILY_SUMMARY.name())
                .withLanguage(challengeEntity.getLang())
                .withTemplateModel(buildChallengeDailySummaryEmailTemplateModel(challengeEntity))
                .withMailMessage(postChallengeMailMessage)
                .withRecipientAddresses(recipientAddress)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public boolean sendEmailToRegistrant(String challengeOwner, Long registrantId, EmailContent emailContent) {
        boolean result = false;
        final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN = 4L;
        final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI = 104L;
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
        if (challengeService.isChallengeOwner(challengeOwner, registrant.getChallengeId())) {
            String csvEmails = registrant.getRegistrantEmail();

            if (emailContent.getTemplateId() == ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN
                    || emailContent.getTemplateId() == ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI) {
                csvEmails = challengeRegistrantService.findRegistrantsByChallengeId(registrant.getChallengeId()).stream()
                        .map(challengeRegistrant -> challengeRegistrant.getRegistrantEmail()).collect(Collectors.joining(","));
            }

            ChallengeEntity challenge = challengeService.findChallengeById(registrant.getChallengeId());
            bindEmailTemplateVariables(emailContent, challenge, registrant);
            emailContent.setRecipients(csvEmails);
            result = sendFeedbackEmail(emailContent);
        }
        return result;
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
    public boolean sendEmailToDailyChallengeRegistrants(String challengeOwner, Long challengeId, Long now, EmailContent emailContent) {
        if (challengeService.isChallengeOwner(challengeOwner, challengeId)) {
            List<ChallengeRegistrantEntity> registrants = challengeRegistrantService.findChallengeRegistrantWithinPeriod(challengeId, now, TimePeriodEnum.TWENTY_FOUR_HOURS);
            String csvEmails = registrants.stream().map(ChallengeRegistrantEntity::getRegistrantEmail).distinct().collect(joining(","));
            emailContent.setRecipients(csvEmails);
        }
        return sendFeedbackEmail(emailContent);
    }

    private Map<String, Object> buildPostChallengeEmailTemplateModel(ChallengeEntity challengeEntity) {
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
        return templateModel;
    }

    private Map<String, Object> buildNotifyEmployerUpdateCriteriaEmailTemplateModel(ChallengeEntity challengeEntity) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("challengeId", String.valueOf(challengeEntity.getChallengeId()));
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));
        return templateModel;
    }

    private String getRecipientAddresses(ChallengeEntity challengeEntity, boolean includeAuthor) {
        Set<String> emails = new HashSet<>(challengeEntity.getReceivedEmails());
        if (includeAuthor) {
            emails.add(challengeEntity.getAuthorEmail());
        }
        return StringUtils.join(emails, ',');
    }

    private Map<String, Object> buildNotifyChallengeTimelineEmailTemplateModel(
            ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity,
            ChallengePhaseEnum challengePhase, int numberOfDays) {
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
        templateModel.put("numberOfDays", numberOfDays);
        templateModel.put("challengePhase", challengePhase.getValue());
        templateModel.put("challengeRegistrant", challengeRegistrantEntity);
        return templateModel;
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

    private Map<String, Object> buildNotifyChallengePhaseClosedEmailTemplateModel(ChallengeEntity challengeEntity,
                                                                                  ChallengePhaseEnum oldPhase, ChallengePhaseEnum currentPhase) {
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
        return templateModel;
    }

    private Map<String, Object> buildConfirmUserJoinChallengeEmailTemplateModel(
            ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) {
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
        return templateModel;
    }

    private Map<String, Object> buildChallengeDailySummaryEmailTemplateModel(ChallengeEntity challengeEntity) {
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
        return templateModel;
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

        if (result.contains(ChallengeEmailService.VAR_CONTEST_NAME)) {
            result = result.replace(ChallengeEmailService.VAR_CONTEST_NAME, challenge.getChallengeName());
        }
        if (result.contains(ChallengeEmailService.VAR_CONTEST_FIRST_PRIZE)) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challenge.getFirstPlaceReward()), Locale.US);
            result = result.replace(ChallengeEmailService.VAR_CONTEST_FIRST_PRIZE, formatPrize);
        }
        if (result.contains(ChallengeEmailService.VAR_CONTESTANT_FIRST_NAME)) {
            result = result.replace(ChallengeEmailService.VAR_CONTESTANT_FIRST_NAME, registrant.getRegistrantFirstName());
        }
        if (result.contains(ChallengeEmailService.VAR_MAIL_SIGNATURE)) {
            result = result.replace(ChallengeEmailService.VAR_MAIL_SIGNATURE, emailSettingDto.getEmailSignature());
        }
        if (result.contains(ChallengeEmailService.VAR_CONTEST_SECOND_PRIZE) && challenge.getSecondPlaceReward() != null) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challenge.getSecondPlaceReward()), Locale.US);
            formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
            result = result.replace(ChallengeEmailService.VAR_CONTEST_SECOND_PRIZE, formatPrize);
        }
        if (result.contains(ChallengeEmailService.VAR_CONTEST_THIRD_PRIZE) && challenge.getThirdPlaceReward() != null) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challenge.getThirdPlaceReward()), Locale.US);
            formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
            result = result.replace(ChallengeEmailService.VAR_CONTEST_THIRD_PRIZE, formatPrize);
        }

        Set<ChallengeRegistrantDto> winners = challengeRegistrantService.findWinnerRegistrantsByChallengeId(challenge.getChallengeId());
        Optional<ChallengeRegistrantDto> firstWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.FIRST_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> secondWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.SECOND_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> thirdWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.THIRD_PLACE).findFirst();

        if (result.contains(ChallengeEmailService.VAR_FIRST_WINNER_FIRST_NAME)
                || result.contains(ChallengeEmailService.VAR_FIRST_WINNER_FULL_NAME)) {
            if (firstWinner.isPresent()) {
                String firstName = firstWinner.get().getRegistrantFirstName();
                String lastName = firstWinner.get().getRegistrantLastName();
                result = result.replace(ChallengeEmailService.VAR_FIRST_WINNER_FIRST_NAME, firstName);
                result = result.replace(ChallengeEmailService.VAR_FIRST_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        if (result.contains(ChallengeEmailService.VAR_SECOND_WINNER_FIRST_NAME)
                || result.contains(ChallengeEmailService.VAR_SECOND_WINNER_FULL_NAME)) {
            if (secondWinner.isPresent()) {
                String firstName = secondWinner.get().getRegistrantFirstName();
                String lastName = secondWinner.get().getRegistrantLastName();
                result = result.replace(ChallengeEmailService.VAR_SECOND_WINNER_FIRST_NAME, firstName);
                result = result.replace(ChallengeEmailService.VAR_SECOND_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        if (result.contains(ChallengeEmailService.VAR_THIRD_WINNER_FIRST_NAME)
                || result.contains(ChallengeEmailService.VAR_THIRD_WINNER_FULL_NAME)) {
            if (thirdWinner.isPresent()) {
                String firstName = thirdWinner.get().getRegistrantFirstName();
                String lastName = thirdWinner.get().getRegistrantLastName();
                result = result.replace(ChallengeEmailService.VAR_THIRD_WINNER_FIRST_NAME, firstName);
                result = result.replace(ChallengeEmailService.VAR_THIRD_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        return result;
    }

}
