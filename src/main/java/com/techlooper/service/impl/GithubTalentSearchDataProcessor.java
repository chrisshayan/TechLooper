package com.techlooper.service.impl;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.Talent;
import com.techlooper.service.TalentSearchDataProcessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by NguyenDangKhoa on 3/11/15.
 */
@Service("GITHUBTalentSearchDataProcessor")
public class GithubTalentSearchDataProcessor implements TalentSearchDataProcessor {

    @Override
    public List<Talent> process(List<UserImportEntity> users) {
        return users.stream().map(userImportEntity -> {
            Map<String,Object> profile = (Map<String,Object>) userImportEntity.getProfiles().get(SocialProvider.GITHUB);

            if (profile == null) {
                return null;
            }

            Talent.Builder talentBuilder = new Talent.Builder();
            return talentBuilder.withEmail(userImportEntity.getEmail())
                             .withUsername(profile.get("username").toString())
                             .withFullName(userImportEntity.getFullName())
                             .withImageUrl(profile.get("imageUrl").toString())
                             .withCompany(profile.get("company").toString())
                             .withDescription(profile.get("description").toString())
                             .withLocation(profile.get("location").toString())
                             .withSkills(((List<String>)profile.get("skills")))
                             .build();
        }).filter(talent -> talent != null).collect(Collectors.toList());
    }

}
