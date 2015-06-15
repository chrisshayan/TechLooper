techlooper.controller('getPromotedController', function ($scope, validatorService, vnwConfigService, $http, $translate, utils) {
  $scope.selectize = vnwConfigService;

  var state = {
    default: {},

    result: {
      showResult: true,
      showView: function (viewName) {
        switch (viewName) {
          case "no-promotion-result":
            return $scope.masterPromotion.result.totalJob === 0;

          case "has-promotion-result":
            return $scope.masterPromotion.result.totalJob > 0;

          case "sent-promoted-email-no-result":
            var hasResult = $scope.masterPromotion.result.totalJob > 0;
            var sentEmail = $scope.promotionEmailForm.$sentEmail;
            return !hasResult && sentEmail;

          case "sent-promoted-email-has-result":
            var hasResult = $scope.masterPromotion.result.totalJob > 0;
            var sentEmail = $scope.promotionEmailForm.$sentEmail;
            return hasResult && sentEmail;
        }
        return false;
      }
    }
  }

  $scope.changeState = function (st) {
    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
  }

  $scope.doPromotion = function () {
    if (!$scope.promotionForm.$valid) {
      return false;
    }
    $scope.masterPromotion = angular.copy($scope.promotionInfo);
    $http.post("getPromoted", $scope.promotionInfo)
      .success(function (data, status, headers, config) {
        $scope.masterPromotion.result = data;
        $scope.changeState('result');
      });
  }

  $scope.$watch("masterPromotion", function (newVal, oldVal) {
    if (!newVal && !oldVal) {
      return false;
    }
    $scope.masterPromotion.jobLevelIds = vnwConfigService.getJobLevelIds($scope.masterPromotion.jobLevelId);
    $scope.masterPromotion.jobLevelTitle = vnwConfigService.getJobLevelText($scope.masterPromotion.jobLevelId);
    $scope.masterPromotion.jobCategoryTitle = vnwConfigService.getIndustryTexts($scope.masterPromotion.jobCategoryIds).join(" | ");
  });

  $scope.sendPromotionEmail = function () {
    if (!$scope.promotionEmailForm.$valid) {
      return false;
    }

    var promotionInfo = angular.copy($scope.masterPromotion);
    utils.removeRedundantAttr(promotionInfo, ["result", "jobCategoryTitle", "jobLevelTitle", "jobLevelId"]);
    $http
      .post("getPromoted/email", {
        getPromotedRequest: promotionInfo,
        lang: $translate.use(),
        email: $scope.promotionEmail
      })
      .success(function (data, status, headers, config) {
        $scope.promotionEmailForm.$sentEmail = true;
      });
  }

  $scope.changeState("default");
});