techlooper.factory("userPromotionService", function (vnwConfigService, utils) {
  return {
    refinePromotionInfo: function(promotionInfo) {
      var jobLevelIds = vnwConfigService.getJobLevelIds(promotionInfo.jobLevelIds);
      promotionInfo.jobLevelIds = jobLevelIds ? jobLevelIds : [];

      var jobLevelText = vnwConfigService.getJobLevelText(promotionInfo.jobLevelIds);
      promotionInfo.jobLevelTitle = jobLevelText;

      var industryTexts = vnwConfigService.getIndustryTexts(promotionInfo.jobCategoryIds);
      promotionInfo.jobCategoryTitle = industryTexts ? industryTexts.join(" | ") : undefined;

      utils.removeRedundantAttrs(promotionInfo, ["email", "createdDateTime", "getPromotedResult", "hasResult", "lang", "utm_source", "utm_medium", "utm_campaign"]);
      return promotionInfo;
    }
  }
});