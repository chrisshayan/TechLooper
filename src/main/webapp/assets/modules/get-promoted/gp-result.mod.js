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
          utils.removeRedundantAttrs(scope.promotionEmailForm, ["$sentEmail"]);
          console.log(scope.promotionEmailForm);
        });

        scope.sendPromotionEmail = function () {
          if (!scope.promotionEmailForm.$valid) {
            return false;
          }

          var promotionInfo = angular.copy(scope.masterPromotion);
          utils.removeRedundantAttrs(promotionInfo, ["result", "jobCategoryTitle", "jobLevelTitle", "jobLevelId"]);
          $http
            .post("getPromoted/email", {
              getPromotedRequest: promotionInfo,
              lang: $translate.use(),
              email: scope.promotionEmail
            })
            .success(function (data, status, headers, config) {
              scope.promotionEmailForm.$sentEmail = true;
            });
        }
      }
    }
  });