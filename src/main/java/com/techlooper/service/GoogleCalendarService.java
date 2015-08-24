package com.techlooper.service;

import com.techlooper.dto.WebinarInfoDto;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by phuonghqh on 8/17/15.
 */
public interface GoogleCalendarService {

  WebinarInfoDto createWebinarInfo(WebinarInfoDto webinarInfoDto, String organiser) throws IOException;

  Collection<WebinarInfoDto> findNotExpiredWebinars();

}
