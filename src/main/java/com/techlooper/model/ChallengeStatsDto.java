package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 7/24/15.
 */
public class ChallengeStatsDto {

    private Long numberOfChallenges;

    private Long numberOfRegistrants;

    private Double totalPrizeAmount;

    public Long getNumberOfChallenges() {
        return numberOfChallenges;
    }

    public void setNumberOfChallenges(Long numberOfChallenges) {
        this.numberOfChallenges = numberOfChallenges;
    }

    public Long getNumberOfRegistrants() {
        return numberOfRegistrants;
    }

    public void setNumberOfRegistrants(Long numberOfRegistrants) {
        this.numberOfRegistrants = numberOfRegistrants;
    }

    public Double getTotalPrizeAmount() {
        return totalPrizeAmount;
    }

    public void setTotalPrizeAmount(Double totalPrizeAmount) {
        this.totalPrizeAmount = totalPrizeAmount;
    }
}
