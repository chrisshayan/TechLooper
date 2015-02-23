package com.techlooper.service.impl;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.entity.userimport.VietnamworksUserImportProfile;
import com.techlooper.model.UserImportData;
import com.techlooper.service.UserImportDataProcessor;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 02/11/15.
 */
@Service("VIETNAMWORKSUserImportDataProcessor")
public class VietnamworksUserImportDataProcessor implements UserImportDataProcessor {

  @Resource
  private Mapper dozerMapper;

  public List<UserImportEntity> process(List<UserImportData> users) {
    List<UserImportEntity> userImportEntities = new ArrayList<>();
    for (UserImportData user : users) {
      processEmailAddress(user);
      UserImportEntity userImportEntity = dozerMapper.map(user, UserImportEntity.class);
      userImportEntity.withProfile(user.getCrawlerSource(), dozerMapper.map(user, VietnamworksUserImportProfile.class));
      userImportEntities.add(userImportEntity);
    }
    return userImportEntities;
  }

  private void processEmailAddress(UserImportData user) {
    // Because ElasticSearch will accept field "email" as a key, so we should prepare email for it before adding to ES
    if (StringUtils.isEmpty(user.getEmailAddress())) {
      user.setEmail(user.getUserId() + "@missing.com");
    } else {
      user.setEmail(user.getEmailAddress());
    }
  }

}
