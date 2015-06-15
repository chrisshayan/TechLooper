techlooper.controller('getPromotedController', function ($scope, validatorService, vnwConfigService) {
  $scope.selectize = vnwConfigService;

  var state = {
    default: {
    },

    result: {
      showResult: true
    }
  }

  $scope.changeState = function(st) {
    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
  }

  $scope.$watch("promotionInfo.jobTitle", function(newVal, oldVal) {
    if (!newVal && !oldVal) {
      return;
    }

    $scope.promotionForm.jobTitle.$touch = true;
  });

  $scope.doPromotion = function() {
    if ($scope.promotionForm.$valid) {
      $scope.masterPromotion = angular.copy($scope.promotionInfo);
      $scope.changeState('result');
    }
  }


  $scope.$watch("masterPromotion", function(newVal, oldVal) {
    if (!newVal && !oldVal) {
      return;
    }
    $scope.masterPromotion.jobLevelTitle = vnwConfigService.getJobLevelText($scope.masterPromotion.jobLevelId);
    $scope.masterPromotion.jobCategoryTitle = vnwConfigService.getIndustryTexts($scope.masterPromotion.jobCategoryIds).join(" | ");
  });

  $scope.changeState("default");
});