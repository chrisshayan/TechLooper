package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 7/27/15.
 */
public class PersonalHomepageDto {

    private TermStatisticResponse termStatistic;

    private ChallengeDetailDto latestChallenge;

    public TermStatisticResponse getTermStatistic() {
        return termStatistic;
    }

    public void setTermStatistic(TermStatisticResponse termStatistic) {
        this.termStatistic = termStatistic;
    }

    public ChallengeDetailDto getLatestChallenge() {
        return latestChallenge;
    }

    public void setLatestChallenge(ChallengeDetailDto latestChallenge) {
        this.latestChallenge = latestChallenge;
    }
}
