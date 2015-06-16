techlooper.controller('getPromotedController', function ($scope, utils, vnwConfigService, $q, userPromotionService, $http, $location) {
  $scope.selectize = vnwConfigService;

  var state = {
    default: {},

    result: {
      showResult: true,

      showView: function (viewName) {
        var promotionResult = $scope.masterPromotion.result;
        var promotionEmailForm = $scope.promotionEmailForm;
        switch (viewName) {
          case "no-promotion-result":
            return promotionResult && promotionResult.totalJob === 0;

          case "has-promotion-result":
            return promotionResult && promotionResult.totalJob > 0;

          case "sent-promoted-email-no-result":
            var hasResult = promotionResult && promotionResult.totalJob > 0;
            var sentEmail = promotionEmailForm && promotionEmailForm.$sentEmail;
            return !hasResult && sentEmail;

          case "sent-promoted-email-has-result":
            var hasResult = promotionResult && promotionResult.totalJob > 0;
            var sentEmail = promotionEmailForm && promotionEmailForm.$sentEmail;
            return hasResult && sentEmail;

          case "not-sent-promoted-email":
            var sentEmail = promotionEmailForm && promotionEmailForm.$sentEmail;
            return !sentEmail;
        }
        return false;
      }
    }
  }

  $scope.viewsDefers = {getPromotedForm: $q.defer(), getPromotedResults: $q.defer()};
  var viewsPromises = utils.toPromises($scope.viewsDefers);
  $q.all(viewsPromises).then(function(data) {
    var doPromotionWithParam = function(promotionInfo, forceValidation) {
      $scope.promotionInfo = angular.copy(userPromotionService.refinePromotionInfo(promotionInfo));
      $scope.doPromotion(forceValidation);
    }

    //http://localhost:8080/#/get-promoted?jobTitle=java&jobLevelIds=[5,6]&jobCategoryIds=[35,55,57]&lang=en&utm_source=getpromotedemail&utm_medium=skilltrendsbutton&utm_campaign=howtogetpromoted
    var param = $location.search();
    if (param.id) {
      $http.get("getPromotedResult/" + param.id).success(function (data, status, headers, config) {
        doPromotionWithParam(data, true);
      });
    }
    else if (!$.isEmptyObject(param)) {
      param = utils.toObject(param);
      doPromotionWithParam(param);
    }
  });

  $scope.changeState = function (st) {
    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
    $scope.$emit("stateChanged")
  }

  $scope.changeState("default");
});