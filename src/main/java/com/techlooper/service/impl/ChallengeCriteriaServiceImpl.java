package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantCriteria;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.model.ChallengeRegistrantCriteriaDto;
import com.techlooper.model.ChallengeRegistrantCriteriaDto.ChallengeRegistrantCriteriaDtoBuilder;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import com.techlooper.util.DataUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by phuonghqh on 10/16/15.
 */
@Service
public class ChallengeCriteriaServiceImpl implements ChallengeCriteriaService {

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Resource
    private Mapper dozerMapper;

    public ChallengeCriteriaDto saveChallengeCriteria(ChallengeCriteriaDto challengeCriteriaDto, String ownerEmail) {
        ChallengeEntity challenge = challengeService.findChallengeById(challengeCriteriaDto.getChallengeId());
        if (challenge == null) {
            return null;
        }

        Set<ChallengeCriteria> criteria = new HashSet<>();
        challengeCriteriaDto.getChallengeCriteria().forEach(cri -> {
            if (cri.getCriteriaId() == null) cri.setCriteriaId(DataUtils.generateStringId());
            criteria.add(cri);
        });

        Set<ChallengeRegistrantEntity> challengeRegistrantEntities = new HashSet<>();
        Iterator<ChallengeRegistrantEntity> challengeIterator = challengeRegistrantRepository.search(
                QueryBuilders.termQuery("challengeId", challengeCriteriaDto.getChallengeId())).iterator();

        while (challengeIterator.hasNext()) {
            final ChallengeRegistrantEntity registrantEntity = challengeIterator.next();
            final Set<ChallengeRegistrantCriteria> registrantCriteria = new HashSet<>();

            criteria.forEach(challengeCri -> {
                ChallengeRegistrantCriteria registrantCri = registrantEntity.getCriteria().stream()
                        .filter(cri -> challengeCri.getCriteriaId().equals(cri.getCriteriaId()))
                        .findFirst().orElse(new ChallengeRegistrantCriteria());
                dozerMapper.map(challengeCri, registrantCri);
                registrantCriteria.add(registrantCri);
                registrantEntity.getCriteria().remove(registrantCri);
            });
            registrantEntity.setCriteria(registrantCriteria);
            challengeRegistrantEntities.add(registrantEntity);
        }

        challenge.setCriteria(criteria);
        if (challengeRegistrantEntities.size() > 0) {
            Set<ChallengeRegistrantCriteriaDto> registrantCriteria = new HashSet<>();
            challengeRegistrantRepository.save(challengeRegistrantEntities)
                    .forEach(registrant -> registrantCriteria.add(toChallengeRegistrantCriteriaDto(registrant)));
            challengeCriteriaDto.setRegistrantCriteria(registrantCriteria);
        }

        challenge = challengeRepository.save(challenge);
        challengeCriteriaDto.setChallengeId(challenge.getChallengeId());
        challengeCriteriaDto.setChallengeCriteria(challenge.getCriteria());
        return challengeCriteriaDto;
    }

    public ChallengeRegistrantCriteriaDto saveScoreChallengeRegistrantCriteria(ChallengeRegistrantCriteriaDto registrantCriteriaDto, String ownerEmail) {
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantCriteriaDto.getRegistrantId());
        ChallengeEntity challenge = challengeService.findChallengeById(registrant.getChallengeId(), ownerEmail);
        if (challenge == null) {
            return null;
        }

        registrant.getCriteria().forEach(cri -> {
            ChallengeRegistrantCriteria criteriaDto = registrantCriteriaDto.getCriteria().stream()
                    .filter(criDto -> criDto.getCriteriaId().equals(cri.getCriteriaId())).findFirst().get();

            // only save score and comment
            cri.setScore(criteriaDto.getScore());
            cri.setComment(criteriaDto.getComment());
            registrantCriteriaDto.getCriteria().remove(criteriaDto);
        });

        registrant = challengeRegistrantRepository.save(registrant);
        return toChallengeRegistrantCriteriaDto(registrant);
    }

    public ChallengeRegistrantCriteriaDto findByChallengeRegistrantId(Long registrantId, String ownerEmail) {
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
        if (!challengeService.isOwnerOfChallenge(ownerEmail, registrant.getChallengeId())) {
            return null;
        }

        return ChallengeRegistrantCriteriaDtoBuilder.challengeRegistrantCriteriaDto()
                .withRegistrantId(registrantId).withCriteria(registrant.getCriteria()).build();
    }

    private ChallengeRegistrantCriteriaDto toChallengeRegistrantCriteriaDto(ChallengeRegistrantEntity registrantEntity) {
        return ChallengeRegistrantCriteriaDtoBuilder.challengeRegistrantCriteriaDto()
                .withRegistrantId(registrantEntity.getRegistrantId())
                .withCriteria(registrantEntity.getCriteria()).build();
    }
}
