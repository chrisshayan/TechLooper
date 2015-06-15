techlooper.controller('getPromotedController', function ($scope, validatorService, vnwConfigService, $http) {
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
    $scope.masterPromotion.jobLevelTitle = vnwConfigService.getJobLevelText($scope.masterPromotion.jobLevelId);
    $scope.masterPromotion.jobCategoryTitle = vnwConfigService.getIndustryTexts($scope.masterPromotion.jobCategoryIds).join(" | ");
  });

  $scope.sendPromotionEmail = function () {
    if (!$scope.promotionEmailForm.$valid) {
      return false;
    }
    //$http.post("getPromoted", $scope.promotionInfo)
    //  .success(function (data, status, headers, config) {
    //    $scope.masterPromotion.result = data;
    //    $scope.changeState('result');
    //  });
  }

  $scope.changeState("default");
});