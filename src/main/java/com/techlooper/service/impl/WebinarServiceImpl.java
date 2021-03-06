package com.techlooper.service.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.techlooper.dto.JoinBySocialDto;
import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.CalendarInfo;
import com.techlooper.entity.WebinarEntity;
import com.techlooper.model.EmailRequestModel;
import com.techlooper.model.EmailTemplateNameEnum;
import com.techlooper.model.SocialConfig;
import com.techlooper.model.UserProfileDto;
import com.techlooper.repository.elasticsearch.WebinarRepository;
import com.techlooper.service.CompanyService;
import com.techlooper.service.EmailService;
import com.techlooper.service.WebinarService;
import freemarker.template.TemplateException;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

/**
 * Created by phuonghqh on 8/18/15.
 */
@Service
public class WebinarServiceImpl implements WebinarService {

    @Resource
    private WebinarRepository webinarRepository;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private Calendar googleCalendar;

    @Resource
    private CompanyService companyService;

    @Resource
    private SocialConfig googleSocialConfig;

    @Resource
    private MimeMessage alertEventOrganiserMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private EmailService emailService;

    public WebinarInfoDto createWebinarInfo(WebinarInfoDto webinarInfoDto, UserProfileDto organiser) throws IOException {

        Event event = new Event()
                .setSummary(webinarInfoDto.getName())
                .setDescription(webinarInfoDto.getDescription());

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");
        org.joda.time.DateTime startDate = dateTimeFormatter.parseDateTime(webinarInfoDto.getStartDate());
        org.joda.time.DateTime endDate = dateTimeFormatter.parseDateTime(webinarInfoDto.getEndDate());
        event.setStart(new EventDateTime().setDateTime(new DateTime(startDate.toString())));
        event.setEnd(new EventDateTime().setDateTime(new DateTime(endDate.toString())));

        Set<UserProfileDto> webinarAttendees = webinarInfoDto.getAttendees();
        webinarAttendees.add(organiser);

        EventAttendee[] attendees = webinarAttendees.stream()
                .map(attEmail -> new EventAttendee().setEmail(attEmail.getEmail()))
                .toArray(EventAttendee[]::new);

        event.setAttendees(Arrays.asList(attendees));

        event = googleCalendar.events().insert(googleSocialConfig.getCalendarId(), event).setSendNotifications(true).execute();

        webinarAttendees.remove(organiser);//Remove organiser
        WebinarEntity entity = dozerMapper.map(webinarInfoDto, WebinarEntity.class);
        entity.setCalendarInfo(dozerMapper.map(event, CalendarInfo.class));
        entity.setOrganiser(organiser);
        entity = webinarRepository.save(entity);
        return dozerMapper.map(entity, WebinarInfoDto.class);
    }

    public Collection<WebinarInfoDto> findAllWebinars() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withIndices("techlooper").withTypes("webinar")
                .withSort(SortBuilders.fieldSort("startDate").order(SortOrder.ASC));
//      .withFilter(FilterBuilders.rangeFilter("startDate").from(org.joda.time.DateTime.now().toString("dd/MM/yyyy hh:mm a")));

        List<WebinarInfoDto> webinarInfoDtos = new ArrayList<>();
        int pageIndex = 0;
        while (true) {
            queryBuilder.withPageable(new PageRequest(pageIndex++, 100));
            FacetedPage<WebinarEntity> page = webinarRepository.search(queryBuilder.build());
            if (!page.hasContent()) {
                break;
            }

            page.spliterator().forEachRemaining(webinarEntity -> {
                webinarInfoDtos.add(dozerMapper.map(webinarEntity, WebinarInfoDto.class));
            });
        }

        return webinarInfoDtos;
    }

    public List<WebinarInfoDto> listUpcomingWebinar() {
        List<WebinarInfoDto> upcomingWebinars = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("webinar");
        searchQueryBuilder.withPageable(new PageRequest(0, 4)).withQuery(QueryBuilders.rangeQuery("startDate").from("now"))
                .withSort(SortBuilders.fieldSort("startDate").order(SortOrder.ASC));

        List<WebinarEntity> webinarEntities = webinarRepository.search(searchQueryBuilder.build()).getContent();
        if (!webinarEntities.isEmpty()) {
            for (WebinarEntity webinarEntity : webinarEntities) {
                WebinarInfoDto webinarInfoDto = dozerMapper.map(webinarEntity, WebinarInfoDto.class);
                upcomingWebinars.add(webinarInfoDto);
            }
        }

//    Collections.reverse(upcomingWebinars);
        return upcomingWebinars;//.stream().limit(4).collect(Collectors.toList());
    }

    public WebinarInfoDto findWebinarById(Long id) {
        WebinarEntity entity = webinarRepository.findOne(id);
        if (entity != null) {
            WebinarInfoDto webinarInfoDto = dozerMapper.map(entity, WebinarInfoDto.class);
            Optional.ofNullable(companyService.findByUserName(entity.getOrganiser().getUsername())).ifPresent(webinarInfoDto::setCompany);
            return webinarInfoDto;
        }
        return null;
    }

    public WebinarInfoDto joinWebinar(JoinBySocialDto joinBySocialDto) throws IOException, MessagingException, TemplateException {
        WebinarEntity webinar = webinarRepository.findOne(joinBySocialDto.getId());
        if (webinar.getAttendees().size() == 100) {
            return dozerMapper.map(webinar, WebinarInfoDto.class);
        }

        UserProfileDto attendee = dozerMapper.map(joinBySocialDto, UserProfileDto.class);
        if (webinar.getAttendees().add(attendee)) {
            Event event = googleCalendar.events().get(googleSocialConfig.getCalendarId(), webinar.getCalendarInfo().getId()).execute();
            EventAttendee att = new EventAttendee().setEmail(attendee.getEmail())
                    .setDisplayName(String.format("%s %s", joinBySocialDto.getFirstName(), joinBySocialDto.getLastName()));
            List<EventAttendee> attendees = Optional.ofNullable(event.getAttendees()).orElseGet(ArrayList::new);
            attendees.add(att);
            event = googleCalendar.events().update(googleSocialConfig.getCalendarId(), webinar.getCalendarInfo().getId(), event)
                    .setSendNotifications(true).execute();
            webinar.setCalendarInfo(dozerMapper.map(event, CalendarInfo.class));
            sendContestApplicationEmail(webinar, attendee);
        }
        return dozerMapper.map(webinarRepository.save(webinar), WebinarInfoDto.class);
    }

    private void sendContestApplicationEmail(WebinarEntity webinarEntity, UserProfileDto attendee) {
        List<String> subjectVariableValues = Arrays.asList(webinarEntity.getName());
        String recipientAddress = webinarEntity.getOrganiser().getEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.WEBINAR_ALERT_ORGANISER.name())
                .withLanguage(webinarEntity.getLang())
                .withTemplateModel(buildAlertOrganiserEmailTemplateModel(webinarEntity, attendee))
                .withMailMessage(alertEventOrganiserMailMessage)
                .withRecipientAddresses(recipientAddress)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    private Map<String, Object> buildAlertOrganiserEmailTemplateModel(WebinarEntity webinarEntity, UserProfileDto attendee) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("webinar", webinarEntity);
        templateModel.put("attendee", attendee);

        StringBuilder webinarNameAlias = new StringBuilder("");
        webinarNameAlias.append(webinarEntity.getName().replaceAll("\\W", "-"));
        webinarNameAlias.append("-");
        webinarNameAlias.append(String.valueOf(webinarEntity.getCreatedDateTime()));
        webinarNameAlias.append("-id");
        templateModel.put("webinarNameAlias", webinarNameAlias.toString());
        return templateModel;
    }
}
