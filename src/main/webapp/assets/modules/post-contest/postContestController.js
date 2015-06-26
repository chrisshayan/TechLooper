techlooper.controller("postContestController", function ($scope) {

  var state = {
    challenge: {
      showChallenge: true,
      status: function (type) {
        switch (type) {
          case "is-form-valid":
            $scope.challengeForm.$setSubmitted();
            return $scope.challengeForm.$valid();

          case "challenge-tab-class":
            return "active";
        }
      },
      nextState: "timeline"
    },

    timeline: {
      showTimeline: true,
      status: function (type) {
        switch (type) {
          case "is-form-valid":
            $scope.timelineForm.$setSubmitted();
            return $scope.timelineForm.$valid();

          case "challenge-tab-class":
          case "timeline-tab-class":
            return true;
        }
      },
      nextState: "reward"
    },

    reward: {
      showReward: true,
      status: function (type) {
        switch (type) {
          case "is-form-valid":
            $scope.rewardForm.$setSubmitted();
            return $scope.rewardForm.$valid();

          case "is-challenge-tab-active":
          case "is-timeline-tab-active":
          case "is-reward-tab-active":
            return true;
        }
      }
    }
  }

  $scope.changeState = function (st) {
    if (!st) {
      return;
    }

    //if ($scope.state && !$scope.state.status("is-form-valid")) {
    //  return false;
    //}

    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
    $scope.$emit("$stateChangeSuccess");
  }

  $scope.nextState = function () {
    $scope.changeState($scope.state.nextState);
  }

  $scope.changeState(state.challenge);
});