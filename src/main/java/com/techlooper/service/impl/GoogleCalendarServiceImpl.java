package com.techlooper.service.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.WebinarEntity;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.repository.userimport.WebinarRepository;
import com.techlooper.repository.vnw.VnwUserRepo;
import com.techlooper.service.GoogleCalendarService;
import org.dozer.Mapper;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by phuonghqh on 8/18/15.
 */
@Service
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

  @Resource
  private WebinarRepository webinarRepository;

  @Resource
  private Mapper dozerMapper;

  @Resource
  private Calendar googleCalendar;

  @Resource
  private VnwUserRepo vnwUserRepo;

  private static final String CALENDAR_ID = "techlooperawesome@gmail.com";

  public WebinarInfoDto createWebinarInfo(WebinarInfoDto webinarInfoDto, String organiser) throws IOException {
    VnwUser vnwUser = vnwUserRepo.findByUsernameIgnoreCase(organiser);
    String organiserEmail = vnwUser != null ? vnwUser.getEmail() : organiser;

    Event event = new Event()
      .setSummary(webinarInfoDto.getName())
      .setDescription(webinarInfoDto.getDescription());

    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");
    org.joda.time.DateTime startDate = dateTimeFormatter.parseDateTime(webinarInfoDto.getStartDate());
    org.joda.time.DateTime endDate = dateTimeFormatter.parseDateTime(webinarInfoDto.getEndDate());
    event.setStart(new EventDateTime().setDateTime(new DateTime(startDate.toString())));
    event.setEnd(new EventDateTime().setDateTime(new DateTime(endDate.toString())));

    webinarInfoDto.getAttendees().add(organiserEmail);
    EventAttendee[] attendees = webinarInfoDto.getAttendees().stream()
      .map(attEmail -> new EventAttendee().setEmail(attEmail))
      .toArray(EventAttendee[]::new);

    event.setAttendees(Arrays.asList(attendees));

    event = googleCalendar.events().insert(CALENDAR_ID, event).setSendNotifications(true).execute();

    WebinarEntity entity = dozerMapper.map(webinarInfoDto, WebinarEntity.class);
    entity.setCalendarUrl(event.getHtmlLink());
    entity.setHangoutLink(event.getHangoutLink());

    entity.setOrganiser(organiserEmail);

    entity = webinarRepository.save(entity);
    return dozerMapper.map(entity, WebinarInfoDto.class);
  }

  public Collection<WebinarInfoDto> findAvailableWebinars() {
    AggregationBuilders.dateHistogram("availableWebinars").field("startDate").format("");

    return null;
  }
}
