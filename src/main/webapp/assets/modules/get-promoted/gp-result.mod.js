techlooper
  .directive("getPromotedResults", function ($http, $translate, utils) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/get-promoted/gp-result.tem.html",
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
          var promotionResult = scope.masterPromotion.result;
          utils.removeRedundantAttrs(promotionInfo, ["result", "jobCategoryTitle", "jobLevelTitle", "jobLevelId"]);
          $http
            .post("getPromoted/email", {
              getPromotedRequest: promotionInfo,
              lang: $translate.use(),
              email: scope.promotionEmail,
              hasResult: promotionResult && promotionResult.totalJob > 0
            })
            .success(function (data, status, headers, config) {
              scope.promotionEmailForm.$sentEmail = true;
            });
        }
      }
    }
  });