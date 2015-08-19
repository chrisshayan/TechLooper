package com.techlooper.service;

import com.techlooper.dto.WebinarInfoDto;

import java.io.IOException;

/**
 * Created by phuonghqh on 8/17/15.
 */
public interface GoogleCalendarService {

  WebinarInfoDto createWebinarInfo(WebinarInfoDto webinarInfoDto) throws IOException;

}
