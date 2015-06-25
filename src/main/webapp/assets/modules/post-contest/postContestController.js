techlooper.controller("postContestController", function ($scope) {

  var state = {
    challenge: {
      showChallenge: true,
      isValid: function () {
        $scope.challengeForm.$setSubmitted();
        return $scope.challengeForm.$valid();
      }
    },

    timeline: {
      showTimeline: true,
      isValid: function () {
        $scope.timelineForm.$setSubmitted();
        return $scope.timelineForm.$valid();
      }
    },

    reward: {
      showReward: true,
      isValid: function () {
        $scope.rewardForm.$setSubmitted();
        return $scope.rewardForm.$valid();
      }
    }
  }

  $scope.changeState = function (st) {
    if ($scope.state && !$scope.state.isValid()) {
      return false;
    }

    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
    $scope.$emit("$stateChangeSuccess");
  }

  $scope.changeState(state.challenge);
});