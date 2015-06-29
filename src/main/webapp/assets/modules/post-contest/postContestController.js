techlooper.controller("postContestController", function ($scope, $http) {
  var state = {
    challenge: {
      showChallenge: true,
      status: function (type) {
        switch (type) {
          case "is-form-valid":
            $scope.challengeForm.$setSubmitted();
            return $scope.challengeForm.$valid;

          case "challenge-tab-class":
            return "active showNavi";
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
            return $scope.timelineForm.$valid;

          case "challenge-tab-class":
            return "active";

          case "timeline-tab-class":
            return "active showNavi";
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
            return $scope.rewardForm.$valid;

          case "challenge-tab-class":
          case "timeline-tab-class":
            return "active";

          case "reward-tab-class":
            return "active showNavi";
        }
      },

      nextState: function () {
        $http.post()
          .success(function(data) {
            console.log(data);
          });
        return true;
      }
    }
  }

  $scope.changeState = function (st) {
    if (!st || ($scope.state && !$scope.state.status("is-form-valid"))) {
      return false;
    }

    if ($.type(st) === "function") {
      return st();
    }

    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
  }

  $scope.nextState = function () {
    $scope.changeState($scope.state.nextState);
  }

  $scope.changeState(state.challenge);
});