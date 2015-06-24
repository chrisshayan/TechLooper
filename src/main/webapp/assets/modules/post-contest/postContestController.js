techlooper.controller("postContestController", function ($scope) {

  var state = {
    challenge: {
      showChallenge: true,
      isValid: function () {
        return true;
      }
    },

    timeline: {
      showTimeline: true,
      isValid: function () {
        return true;
      }
    },

    reward: {
      showReward: true,
      isValid: function () {
        return true;
      }
    }
  }

  $scope.changeState = function (st) {
    if (!$scope.state.isValid()) {
      return false;
    }

    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
    $scope.$emit("$stateChangeSuccess");
  }

  $scope.changeState(state.challenge);
});