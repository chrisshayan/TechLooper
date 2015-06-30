techlooper.directive("userPromotionSurveyForm", function ($http, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/userPromotionSurveyForm.html",
    link: function (scope, element, attr, ctrl) {
      var resetView = function () {
        element.find(".survey-form-block").show();
        element.find(".survey-caret > i").addClass("fa-caret-up");
        element.find(".survey-caret > i").removeClass("fa-caret-down");
      }

      scope.$on("$stateChangeSuccess", function () {
        delete scope.promotionSurvey;
        scope.promotionSurveyForm.$setPristine();
        scope.promotionSurveyForm.$setUntouched();
        utils.removeRedundantAttrs(scope.promotionSurveyForm, ["$sentSurvey"]);
        resetView();
      });

      scope.togglePromotionSurveyView = function () {
        element.find(".survey-form-block").toggle();
        element.find(".survey-caret > i").toggleClass("fa-caret-up");
        element.find(".survey-caret > i").toggleClass("fa-caret-down");
      }

      scope.sendPromotionSurvey = function () {
        if (!scope.promotionSurveyForm.$valid) {
          return false;
        }

        var getPromotedRequest = angular.copy(scope.masterPromotion);
        utils.removeRedundantAttrs(getPromotedRequest, ["survey", "id", "jobLevelTitle", "result", "jobCategoryTitle"]);

        var getPromotedSurvey = angular.copy(scope.promotionSurvey);
        getPromotedSurvey.getPromotedId = scope.masterPromotion.id;

        var request = {getPromotedRequest: getPromotedRequest, getPromotedSurvey: getPromotedSurvey};

        $http.post("getPromoted/survey", request, {transformResponse: function (d, h) {return d;}})
          .success(function (data, status, headers, config) {
            scope.promotionSurveyForm.$sentSurvey = true;
            scope.masterPromotion.id = data;
          });
      }
    }
  }
});