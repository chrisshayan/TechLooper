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
import com.techlooper.service.WebinarService;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

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
    DateHistogramBuilder availableWebinar = AggregationBuilders.dateHistogram("availableWebinars").field("startDate")
      .format("dd/MM/yyyy hh:mm a").interval(DateHistogram.Interval.DAY).order(Histogram.Order.COUNT_DESC);

    NativeSearchQuery queryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT).addAggregation(availableWebinar).build();
    FacetedPage<WebinarEntity> abc = webinarRepository.search(queryBuilder);
    System.out.println(abc);

    return null;
  }
}
