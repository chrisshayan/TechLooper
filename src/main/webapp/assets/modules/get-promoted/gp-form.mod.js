techlooper
  .directive("getPromotedForm", function ($http, vnwConfigService) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/get-promoted/gp-form.tem.html",
      link: function (scope, element, attr, ctrl) {
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
          scope.masterPromotion.jobLevelIds = vnwConfigService.getJobLevelIds(scope.masterPromotion.jobLevelId);
          scope.masterPromotion.jobLevelTitle = vnwConfigService.getJobLevelText(scope.masterPromotion.jobLevelId);
          scope.masterPromotion.jobCategoryTitle = vnwConfigService.getIndustryTexts(scope.masterPromotion.jobCategoryIds).join(" | ");
        });
      }
    }
  });