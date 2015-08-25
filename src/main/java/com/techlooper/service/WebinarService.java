package com.techlooper.service;

import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.UserProfile;
import com.techlooper.model.UserProfileDto;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by phuonghqh on 8/17/15.
 */
public interface WebinarService {

  WebinarInfoDto createWebinarInfo(WebinarInfoDto webinarInfoDto, UserProfileDto organiser) throws IOException;

  Collection<WebinarInfoDto> findAvailableWebinars();

}
