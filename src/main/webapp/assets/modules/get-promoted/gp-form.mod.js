techlooper.directive("getPromotedForm", function ($http, userPromotionService, $location, utils, $q) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.doPromotion = function (forceValidation) {
        //scope.promotionForm.$setSubmitted();
        //if (!forceValidation && !scope.promotionForm.$valid) {
        //  return false;
        //}
        scope.masterPromotion = angular.copy(scope.promotionInfo);
        userPromotionService.refinePromotionInfo(scope.masterPromotion);
        $http.post("getPromoted", scope.masterPromotion).success(function (data, status, headers, config) {
          scope.masterPromotion.result = data;
          scope.changeState('result');
        });

        ga('send', {
          'hitType': 'event',
          'eventCategory': 'salaryreport',
          'eventAction': 'click',
          'eventLabel': 'getpromotedbtn'
        });
      }

      scope.viewsDefers.getPromotedForm.resolve();
    }
  }
});