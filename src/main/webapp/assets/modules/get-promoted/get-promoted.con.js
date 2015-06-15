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
      $scope.changeState('result');
    }
  }

  $scope.changeState("default");
});