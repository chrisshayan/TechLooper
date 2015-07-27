package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 7/27/15.
 */
public class PersonalHomepageDto {

    private TermStatisticResponse termStatistic;

    private ChallengeDetailDto challengeDetail;

    public TermStatisticResponse getTermStatistic() {
        return termStatistic;
    }

    public void setTermStatistic(TermStatisticResponse termStatistic) {
        this.termStatistic = termStatistic;
    }

    public ChallengeDetailDto getChallengeDetail() {
        return challengeDetail;
    }

    public void setChallengeDetail(ChallengeDetailDto challengeDetail) {
        this.challengeDetail = challengeDetail;
    }
}
