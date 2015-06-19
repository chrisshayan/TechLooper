techlooper.directive("userPromotionSurveyForm", function ($http, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/userPromotionSurveyForm.html",
    link: function (scope, element, attr, ctrl) {
      scope.doSurvey = function () {
        var getPromotedRequest = angular.copy(scope.masterPromotion);
        utils.removeRedundantAttrs(getPromotedRequest, ["survey", "id"]);

        var getPromotedSurvey = angular.copy(scope.masterPromotion.survey);
        getPromotedSurvey.getPromotedId = scope.masterPromotion.id;

        var request = {
          getPromotedRequest: getPromotedRequest,
          getPromotedSurvey: getPromotedSurvey
        };//$.extend(true, {}, scope.masterPromotion.survey, {getPromotedId: scope.masterPromotion.id} );
        $http.post("getPromoted/survey", request, {transformResponse: function (d, h) {return d;}})
          .success(function (data, status, headers, config) {
            scope.promotionSurveyForm.$sentSurvey = true;
            scope.masterPromotion.id = data;
          });
      }
    }
  }
});