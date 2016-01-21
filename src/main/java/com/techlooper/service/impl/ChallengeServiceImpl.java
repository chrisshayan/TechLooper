package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantCriteria;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeEmailService;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ChallengeService;
import com.techlooper.util.DataUtils;
import com.techlooper.util.DateTimeUtils;
import com.techlooper.util.EmailValidator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.currentDate;
import static com.techlooper.util.DateTimeUtils.daysBetween;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.sum;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final static ChallengePhaseEnum CHALLENGE_PHASES[] = {FINAL, PROTOTYPE, UIUX, IDEA, REGISTRATION};

    @Resource
    private Mapper dozerMapper;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeEmailService challengeEmailService;

    @Override
    public ChallengeEntity postChallenge(ChallengeDto challengeDto) {
        ChallengeEntity challengeEntity = dozerMapper.map(challengeDto, ChallengeEntity.class);
        if (challengeDto.getChallengeId() == null) {
            challengeEntity.setChallengeId(DateTimeUtils.currentDateTime());
        }

        challengeEntity.setCriteria(DataUtils.defaultChallengeCriterias(challengeEntity.getLang()));
        return challengeRepository.save(challengeEntity);
    }

    @Override
    public Long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto) {
        final long INVALID_REGISTRANT = 0L;
        final int MIN_RANDOM_SEED_NUMBER = 1000;
        final int MAX_RANDOM_SEED_NUMBER = 9999;
        Long challengeId = challengeRegistrantDto.getChallengeId();
        ChallengeEntity challengeEntity = challengeRepository.findOne(challengeId);
        String registrantEmail = challengeRegistrantDto.getRegistrantEmail();

        boolean isValidEmail = checkValidEmailCompareToChallengeType(challengeEntity, registrantEmail);
        if (!isValidEmail) {
            return INVALID_REGISTRANT;
        }

        boolean isExistRegistrant = checkIfRegistrantAlreadyExist(challengeId, registrantEmail);
        if (isExistRegistrant) {
            return INVALID_REGISTRANT;
        }

        ChallengeRegistrantEntity challengeRegistrantEntity = dozerMapper.map(challengeRegistrantDto, ChallengeRegistrantEntity.class);
        challengeRegistrantEntity.setRegistrantId(DateTimeUtils.currentDateTime());
        if (challengeEntity.getCriteria().size() > 0) {
            final Set<ChallengeRegistrantCriteria> criteria = new HashSet<>();
            challengeEntity.getCriteria().forEach(cri -> criteria.add(dozerMapper.map(cri, ChallengeRegistrantCriteria.class)));
            challengeRegistrantEntity.setCriteria(criteria);
        }

        challengeRegistrantEntity.setMailSent(Boolean.TRUE);
        if (challengeEntity.getChallengeType() == ChallengeTypeEnum.INTERNAL) {
            Integer passCode = DataUtils.getRandomNumberInRange(MIN_RANDOM_SEED_NUMBER, MAX_RANDOM_SEED_NUMBER);
            challengeRegistrantEntity.setPassCode(passCode);
        }
        challengeRegistrantEntity = challengeRegistrantRepository.save(challengeRegistrantEntity);
        challengeEmailService.sendApplicationEmailToContestant(challengeEntity, challengeRegistrantEntity);

        return challengeRegistrantService.getNumberOfRegistrants(challengeId);
    }

    @Override
    public List<ChallengeDetailDto> findChallenges(ChallengeFilterCondition allChallengeFilterCondition) {
        NativeSearchQueryBuilder allChallengeQueryBuilder = getChallengeSearchQueryBuilder(allChallengeFilterCondition);
        List<ChallengeEntity> challenges = DataUtils.getAllEntities(challengeRepository, allChallengeQueryBuilder);

        List<ChallengeDetailDto> challengeDetails = new ArrayList<>();
        for (ChallengeEntity challenge : challenges) {
            ChallengeDetailDto challengeDetail = dozerMapper.map(challenge, ChallengeDetailDto.class);
            challengeDetail.setNumberOfRegistrants(challengeRegistrantService.getNumberOfRegistrants(challenge.getChallengeId()));
            challengeDetails.add(challengeDetail);
        }
        return challengeDetails;
    }

    @Override
    public Long getNumberOfChallenges() {
        return challengeRepository.count();
    }

    @Override
    public Double getTotalAmountOfPrizeValues() {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT);
        searchQueryBuilder.withQuery(matchAllQuery());

        String sumScript = "doc['firstPlaceReward'].value + doc['secondPlaceReward'].value + doc['thirdPlaceReward'].value";
        SumBuilder sumPrizeBuilder = sum("sumPrize").script(sumScript);
        searchQueryBuilder.addAggregation(sumPrizeBuilder);

        Aggregations aggregations = elasticsearchTemplateUserImport.query(searchQueryBuilder.build(), SearchResponse::getAggregations);
        Sum sumPrize = aggregations.get("sumPrize");
        return sumPrize != null ? sumPrize.getValue() : 0D;
    }

    @Override
    public ChallengeDetailDto getTheLatestChallenge() {
        ChallengeDetailDto challengeDetailDto = new ChallengeDetailDto();
        ChallengeFilterCondition allChallengeFilterCondition = new ChallengeFilterCondition();
        List<ChallengeDetailDto> challenges = findChallenges(allChallengeFilterCondition);
        if (CollectionUtils.isNotEmpty(challenges)) {
            return challenges.get(0);
        }
        return challengeDetailDto;
    }

    @Override
    public List<ChallengeDetailDto> findChallengeByOwner(String ownerEmail) {
        List<ChallengeDetailDto> challengeDetails = new ArrayList<>();
        ChallengeFilterCondition challengeFilterCondition = new ChallengeFilterCondition();
        challengeFilterCondition.setAuthorEmail(ownerEmail);
        NativeSearchQueryBuilder searchChallengeByOwnerQuery = getChallengeSearchQueryBuilder(challengeFilterCondition);
        List<ChallengeEntity> challenges = DataUtils.getAllEntities(challengeRepository, searchChallengeByOwnerQuery);

        for (ChallengeEntity challenge : challenges) {
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challenge, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(
                    challengeRegistrantService.getNumberOfRegistrants(challengeDetailDto.getChallengeId()));
            challengeDetails.add(challengeDetailDto);
        }
        return challengeDetails;
    }

    @Override
    public boolean deleteChallenge(Long challengeId, String ownerEmail) {
        if (isChallengeOwner(ownerEmail, challengeId)) {
            ChallengeEntity challenge = challengeRepository.findOne(challengeId);
            challenge.setExpired(Boolean.TRUE);
            challengeRepository.save(challenge);
            return true;
        }
        return false;
    }

    @Override
    public List<ChallengeEntity> findChallengeByPhase(ChallengePhaseEnum challengePhase) {
        ChallengeFilterCondition challengeFilterCondition = new ChallengeFilterCondition();
        challengeFilterCondition.setPhase(challengePhase);
        NativeSearchQueryBuilder searchChallengeByPhaseQuery = getChallengeSearchQueryBuilder(challengeFilterCondition);
        return DataUtils.getAllEntities(challengeRepository, searchChallengeByPhaseQuery);
    }

    @Override
    public boolean isChallengeOwner(String ownerEmail, Long challengeId) {
        ChallengeEntity challenge = challengeRepository.findOne(challengeId);
        return challenge.getAuthorEmail().equalsIgnoreCase(ownerEmail);
    }

    @Override
    public ChallengeEntity findChallengeById(Long challengeId) {
        return challengeRepository.findOne(challengeId);
    }

    @Override
    public ChallengeEntity findChallengeById(Long challengeId, String ownerEmail) {
        ChallengeEntity challenge = challengeRepository.findOne(challengeId);
        if (isChallengeOwner(ownerEmail, challengeId)) {
            return challenge;
        }
        return null;
    }

    @Override
    public ChallengeDetailDto getChallengeDetail(Long challengeId, String ownerEmail) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challenge");
        TermQueryBuilder challengeIdQuery = termQuery("challengeId", challengeId);
        TermQueryBuilder expiredChallengeQuery = termQuery("expired", Boolean.TRUE);

        searchQueryBuilder.withQuery(boolQuery().must(challengeIdQuery).mustNot(expiredChallengeQuery));
        List<ChallengeEntity> challengeEntities = DataUtils.getAllEntities(challengeRepository, searchQueryBuilder);

        if (CollectionUtils.isNotEmpty(challengeEntities)) {
            ChallengeEntity challengeEntity = challengeEntities.get(0);
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(challengeRegistrantService.getNumberOfRegistrants(challengeId));
            calculateChallengePhases(challengeDetailDto);
            boolean isAuthor = challengeEntity.getAuthorEmail().equals(ownerEmail);
            challengeDetailDto.setIsAuthor(isAuthor);
            if (!isAuthor) {
                challengeDetailDto.setCriteria(null);
            }

            challengeDetailDto.setPhaseItems(challengeRegistrantService.getChallengeRegistrantFunnel(challengeId, ownerEmail));
            Boolean isClosed = daysBetween(challengeDetailDto.getSubmissionDateTime(), currentDate()) > 0;
            challengeDetailDto.setIsClosed(isClosed);
            return challengeDetailDto;
        }
        return null;
    }

    @Override
    public void calculateChallengePhases(ChallengeDetailDto challengeDetailDto) {
        String now = currentDate();

        String timeline[] = {
                challengeDetailDto.getSubmissionDateTime(),
                challengeDetailDto.getPrototypeSubmissionDateTime(),
                challengeDetailDto.getUxSubmissionDateTime(),
                challengeDetailDto.getIdeaSubmissionDateTime(),
                challengeDetailDto.getRegistrationDateTime()
        };

        int currentIndex = -1;
        int nextIndex = -1;
        for (int i = 0; i < timeline.length; ++i) {
            try {
                String milestone = timeline[i];
                if (daysBetween(now, milestone) >= 0) {
                    nextIndex = currentIndex;
                    currentIndex = i;
                }
            } catch (Exception e) {
                continue;
            }
        }

        if (currentIndex == -1) {//FINAL
            challengeDetailDto.setCurrentPhase(FINAL);
            challengeDetailDto.setNextPhase(FINAL);
        } else {
            challengeDetailDto.setCurrentPhase(CHALLENGE_PHASES[currentIndex]);
            challengeDetailDto.setNextPhase(CHALLENGE_PHASES[nextIndex > -1 ? nextIndex : currentIndex]);
        }
    }

    public ChallengeDetailDto updateVisibleWinner(ChallengeDetailDto challengeDetailDto, String ownerEmail) {
        ChallengeEntity challenge = findChallengeById(challengeDetailDto.getChallengeId(), ownerEmail);
        if (challenge == null) {
            return null;
        }
        challenge.setVisibleWinners(challengeDetailDto.getVisibleWinners());
        challenge = challengeRepository.save(challenge);
        challengeDetailDto = dozerMapper.map(challenge, ChallengeDetailDto.class);
        return challengeDetailDto;
    }

    private NativeSearchQueryBuilder getChallengeSearchQueryBuilder(ChallengeFilterCondition challengeFilterCondition) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challenge");

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        String challengeSearchText = challengeFilterCondition.getChallengeSearchText();
        if (StringUtils.isNotEmpty(challengeSearchText)) {
            boolQueryBuilder.should(matchQuery("challengeName", challengeSearchText));

            StringBuilder domainNameQueryStringBuilder = new StringBuilder();
            domainNameQueryStringBuilder.append("*").append(challengeSearchText).append("*");
            boolQueryBuilder.should(wildcardQuery("companyDomains", domainNameQueryStringBuilder.toString()));
        } else {
            boolQueryBuilder.should(matchAllQuery());
        }

        String ownerEmail = challengeFilterCondition.getAuthorEmail();
        if (StringUtils.isNotEmpty(ownerEmail)) {
            boolQueryBuilder.must(matchQuery("authorEmail", ownerEmail).minimumShouldMatch("100%"));
        }

        BoolFilterBuilder boolFilterBuilder = boolFilter();
        if (challengeFilterCondition.isNotExpired()) {
            boolFilterBuilder.mustNot(termFilter("expired", Boolean.TRUE));
        }

        ChallengePhaseEnum phase = challengeFilterCondition.getPhase();
        if (phase != null) {
            if (StringUtils.isNotEmpty(phase.getFromDateTimeField())) {
                RangeFilterBuilder fromFilter = rangeFilter(phase.getFromDateTimeField()).lt("now/d");
                boolFilterBuilder.must(fromFilter);
            }
            if (StringUtils.isNotEmpty(phase.getToDateTimeField())) {
                RangeFilterBuilder toFilter = rangeFilter(phase.getToDateTimeField()).gte("now/d");
                boolFilterBuilder.must(toFilter);
            }
        }

        ChallengeTypeEnum challengeType = challengeFilterCondition.getChallengeType();
        if (challengeType != null) {
            if (challengeType == ChallengeTypeEnum.INTERNAL) {
                boolFilterBuilder.must(termFilter("challengeType", ChallengeTypeEnum.INTERNAL));
            } else if (challengeType == ChallengeTypeEnum.PUBLIC) {
                boolFilterBuilder.mustNot(termFilter("challengeType", ChallengeTypeEnum.INTERNAL));
            }
        }

        searchQueryBuilder.withQuery(filteredQuery(boolQueryBuilder, boolFilterBuilder));
        searchQueryBuilder.withSort(SortBuilders.fieldSort("startDateTime").order(SortOrder.DESC));
        return searchQueryBuilder;
    }

    private boolean checkValidEmailCompareToChallengeType(ChallengeEntity challengeEntity, String email) {
        if (challengeEntity.getChallengeType() == ChallengeTypeEnum.INTERNAL) {
            if (StringUtils.isEmpty(email)) {
                return false;
            }
            for (String domain : challengeEntity.getCompanyDomains()) {
                if (email.contains(domain)) {
                    return true;
                }
            }
            return false;
        }
        return EmailValidator.validate(email);
    }

    private boolean checkIfRegistrantAlreadyExist(Long challengeId, String email) {
        return challengeRegistrantService.findRegistrantByChallengeIdAndEmail(challengeId, email) != null;
    }
}
