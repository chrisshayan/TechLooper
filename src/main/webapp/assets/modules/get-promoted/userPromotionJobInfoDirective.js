techlooper.directive("userPromotionJobInfo", function ($http, userPromotionService, $location, utils, $translate) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/userPromotionJobInfo.html",
    link: function (scope, element, attr, ctrl) {
      scope.$on("stateChanged", function () {
        delete scope.promotionEmail;
        scope.promotionEmailForm.$setPristine();
        scope.promotionEmailForm.$setUntouched();
        utils.removeRedundantAttrs(scope.promotionEmailForm, ["$sentEmail"]);
      });

      scope.sendPromotionEmail = function () {
        if (!scope.promotionEmailForm.$valid) {
          return false;
        }

        var promotionInfo = angular.copy(scope.masterPromotion);
        if (promotionInfo.jobLevelIds.length === 0) delete promotionInfo.jobLevelIds;

        var promotionResult = scope.masterPromotion.result;
        utils.removeRedundantAttrs(promotionInfo, ["result", "jobCategoryTitle", "jobLevelTitle", "jobLevelId"]);
        $http
          .post("getPromoted/email", {
            getPromotedRequest: promotionInfo,
            lang: $translate.use(),
            email: scope.promotionEmail,
            getPromotedId: scope.masterPromotion.id,
            hasResult: promotionResult && promotionResult.totalJob > 0
          }, {transformResponse: function (d, h) {return d;}})
          .success(function (data, status, headers, config) {
            scope.promotionEmailForm.$sentEmail = true;
            scope.masterPromotion.id = data;
          });
      }

    }
  }
});