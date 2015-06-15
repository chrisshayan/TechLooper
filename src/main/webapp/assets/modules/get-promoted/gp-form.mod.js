techlooper.directive("getPromotedForm", function ($http, vnwConfigService, $location) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html",
    link: function (scope, element, attr, ctrl) {
      var campaign = $location.search();

      scope.doPromotion = function () {
        if (!scope.promotionForm.$valid) {
          return false;
        }
        scope.masterPromotion = angular.copy(scope.promotionInfo);
        $http.post("getPromoted", scope.promotionInfo)
          .success(function (data, status, headers, config) {
            scope.masterPromotion.result = data;
            scope.changeState('result');
          });
      }

      scope.$watch("masterPromotion", function (newVal, oldVal) {
        if (!newVal && !oldVal) return false;
        var jobLevelIds = vnwConfigService.getJobLevelIds(scope.masterPromotion.jobLevelId);
        scope.masterPromotion.jobLevelIds = jobLevelIds ? jobLevelIds : [];

        var jobLevelText = vnwConfigService.getJobLevelText(scope.masterPromotion.jobLevelId);
        scope.masterPromotion.jobLevelTitle = jobLevelText;

        var industryTexts = vnwConfigService.getIndustryTexts(scope.masterPromotion.jobCategoryIds);
        scope.masterPromotion.jobCategoryTitle = industryTexts ? industryTexts.join(" | ") : undefined;
      });
    }
  }
});