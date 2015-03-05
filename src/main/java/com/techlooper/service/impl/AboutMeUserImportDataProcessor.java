package com.techlooper.service.impl;

import com.techlooper.entity.userimport.AboutMeUserImportProfile;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.UserImportData;
import com.techlooper.service.UserImportDataProcessor;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 02/11/15.
 */
@Service("ABOUTMEUserImportDataProcessor")
public class AboutMeUserImportDataProcessor implements UserImportDataProcessor {

    @Resource
    private Mapper dozerMapper;

    public List<UserImportEntity> process(List<UserImportData> users) {
        List<UserImportEntity> userImportEntities = new ArrayList<>();
        for (UserImportData user : users) {
            UserImportEntity userImportEntity = dozerMapper.map(user, UserImportEntity.class);
            userImportEntity.withProfile(user.getCrawlerSource(), dozerMapper.map(user, AboutMeUserImportProfile.class));
            userImportEntities.add(userImportEntity);
        }
        return userImportEntities;
    }

}
