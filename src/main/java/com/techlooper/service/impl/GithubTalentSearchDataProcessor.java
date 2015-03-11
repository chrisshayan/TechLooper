package com.techlooper.service.impl;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.Talent;
import com.techlooper.service.TalentSearchDataProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/11/15.
 */
@Service("GITHUBTalentSearchDataProcessor")
public class GithubTalentSearchDataProcessor implements TalentSearchDataProcessor {

    @Override
    public List<Talent> process(List<UserImportEntity> users) {
        return null;
    }

}
