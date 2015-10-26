package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.entity.ChallengeSubmissionEntity.ChallengeSubmissionEntityBuilder;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ChallengeSubmissionService;
import com.techlooper.util.DateTimeUtils;
import org.dozer.Mapper;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 10/9/15.
 */
@Service
public class ChallengeSubmissionServiceImpl implements ChallengeSubmissionService {

    @Resource
    private ChallengeService challengeService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private ChallengeSubmissionRepository challengeSubmissionRepository;

    public ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto) {
        ChallengeRegistrantEntity registrant = challengeService.findRegistrantByChallengeIdAndEmail(
                challengeSubmissionDto.getChallengeId(), challengeSubmissionDto.getRegistrantEmail());
        if (registrant == null) {
            registrant = challengeService.joinChallengeEntity(dozerMapper.map(challengeSubmissionDto, ChallengeRegistrantDto.class));
        }
        ChallengePhaseEnum activePhase = registrant.getActivePhase() == null ? ChallengePhaseEnum.REGISTRATION : registrant.getActivePhase();

        ChallengeSubmissionEntity challengeSubmissionEntity = dozerMapper.map(challengeSubmissionDto, ChallengeSubmissionEntity.class);
        ChallengeSubmissionEntityBuilder.challengeSubmissionEntity(challengeSubmissionEntity)
                .withChallengeSubmissionId(DateTime.now().getMillis())
                .withRegistrantId(registrant.getRegistrantId())
                .withRegistrantName(String.format("%s %s", registrant.getRegistrantFirstName(), registrant.getRegistrantLastName()))
                .withSubmissionDateTime(DateTimeUtils.currentDate())
                .withSubmissionPhase(activePhase);

        return challengeSubmissionRepository.save(challengeSubmissionEntity);
    }
}
