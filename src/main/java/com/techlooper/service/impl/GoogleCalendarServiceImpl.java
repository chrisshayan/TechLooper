package com.techlooper.service.impl;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.WebinarEntity;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.repository.elasticsearch.WebinarRepository;
import com.techlooper.repository.vnw.VnwUserRepo;
import com.techlooper.service.GoogleCalendarService;
import org.dozer.Mapper;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

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
    Event event = new Event()
      .setSummary(webinarInfoDto.getName())
      .setDescription(webinarInfoDto.getDescription());

    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");
    org.joda.time.DateTime startDate = dateTimeFormatter.parseDateTime(webinarInfoDto.getStartDate());
    org.joda.time.DateTime endDate = dateTimeFormatter.parseDateTime(webinarInfoDto.getEndDate());

    event.setStart(new EventDateTime().setDate(new DateTime(startDate.toDate())));
    event.setEnd(new EventDateTime().setDate(new DateTime(endDate.toDate())));
    event = googleCalendar.events().insert(CALENDAR_ID, event).setSendNotifications(true).execute();

    WebinarEntity entity = dozerMapper.map(webinarInfoDto, WebinarEntity.class);
    entity.setCalendarUrl(event.getHtmlLink());
    entity.setHangoutLink(event.getHangoutLink());

    String email = vnwUserRepo.findByUsernameIgnoreCase(organiser).getEmail();
    entity.setOrganiser(email);

    entity = webinarRepository.save(entity);
    return dozerMapper.map(entity, WebinarInfoDto.class);
  }
}
