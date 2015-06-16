techlooper.directive("getPromotedForm", function ($http, userPromotionService, $location, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.doPromotion = function (forceValidation) {
        scope.promotionForm.$setSubmitted();
        if (!forceValidation && !scope.promotionForm.$valid) {
          return false;
        }
        scope.masterPromotion = angular.copy(scope.promotionInfo);
        userPromotionService.refinePromotionInfo(scope.masterPromotion);
        $http.post("getPromoted", scope.masterPromotion).success(function (data, status, headers, config) {
          scope.masterPromotion.result = data;
          scope.changeState('result');
        });
      }

      var doPromotionWithParam = function(promotionInfo) {
        console.log(promotionInfo);
        scope.promotionInfo = angular.copy(userPromotionService.refinePromotionInfo(promotionInfo));
        scope.doPromotion(true);
      }

      //http://localhost:8080/#/get-promoted?jobTitle=java&jobLevelIds=[5,6]&jobCategoryIds=[35,55,57]&lang=en&utm_source=getpromotedemail&utm_medium=skilltrendsbutton&utm_campaign=howtogetpromoted
      var param = $location.search();
      if (param.id) {
        $http.get("getPromotedResult/" + param.id).success(function (data, status, headers, config) {
          doPromotionWithParam(data);
        });
      }
      else if (!$.isEmptyObject(param)) {
        param = utils.toObject(param);
        doPromotionWithParam(param);
      }
    }
  }
});