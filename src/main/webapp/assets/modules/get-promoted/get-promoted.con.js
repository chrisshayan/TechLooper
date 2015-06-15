techlooper.controller('getPromotedController', function ($scope, validatorService, vnwConfigService) {
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

  $scope.changeState = function (st) {
    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
    $scope.$emit("stateChanged")
  }

  $scope.changeState("default");
});