techlooper.controller("postContestController", function ($scope, $http, jsonValue, $translate, $location, utils,
                                                         resourcesService, $anchorScroll, apiService) {

  var param = $location.search();
  if (param.id && (param.a == "edit")) {
    apiService.findChallengeById(param.id).success(function (data) {
      $scope.contest = data;
    });
  }
  else {
    $location.search({});
  }

  utils.sendNotification(jsonValue.notifications.loading);
  var state = {
    challenge: {
      name: "challenge",
      showChallenge: true,
      status: function (type) {
        switch (type) {
          case "is-form-valid":
            $scope.challengeForm.$setSubmitted();
            return !$scope.challengeForm.$invalid;

          case "set-form-pristine":
            return $scope.challengeForm && $scope.challengeForm.$setPristine();

          case "is-form-pristine":
            return $scope.challengeForm.$pristine;

          case "challenge-tab-class":
            return "active showNavi";
        }
      },
      nextState: "timeline"
    },

    timeline: {
      name: "timeline",
      showTimeline: true,
      status: function (type) {
        switch (type) {
          case "is-form-valid":
            if (!$scope.timelineForm.$valid) return true;
            $scope.timelineForm.$setSubmitted();
            return !$scope.timelineForm.$invalid;

          case "is-form-pristine":
            return $scope.timelineForm.$pristine;

          case "set-form-pristine":
            return $scope.timelineForm.$setPristine();

          case "challenge-tab-class":
            return "active";

          case "timeline-tab-class":
            return "active showNavi";

          case "ex-start-date":
            return moment().add(7, 'day').format('DD/MM/YYYY');

          case "ex-register-date":
            return moment().add(21, 'day').format('DD/MM/YYYY');

          case "ex-submit-date":
            return moment().add(63, 'day').format('DD/MM/YYYY');

          case "start-date-wt-4w":
            if (!$scope.contest.startDate) return true;
            var lastDate = moment().add(4, 'weeks');
            var startDate = moment($scope.contest.startDate, jsonValue.dateFormat);
            return startDate.isBetween(moment(), lastDate, 'day') || startDate.isSame(moment(), "day");

          case "register-date-gt-start-date":
            if (!$scope.contest.registrationDate) return true;
            var lastDate = moment($scope.contest.startDate, jsonValue.dateFormat);
            return moment($scope.contest.registrationDate, jsonValue.dateFormat).isAfter(lastDate, 'day');

          case "submit-date-gt-register-date":
            if (!$scope.contest.submissionDate) return true;
            var lastDate = moment($scope.contest.registrationDate, jsonValue.dateFormat);
            return moment($scope.contest.submissionDate, jsonValue.dateFormat).isAfter(lastDate, 'day');
        }
      },
      nextState: "reward"
    },

    reward: {
      name: "reward",
      showReward: true,
      status: function (type, param) {
        switch (type) {
          case "is-form-valid":
            $scope.rewardForm.$setSubmitted();
            return !$scope.rewardForm.$invalid;

          case "is-form-pristine":
            return $scope.rewardForm.$pristine;

          case "set-form-pristine":
            return $scope.rewardForm.$setPristine();

          case "challenge-tab-class":
          case "timeline-tab-class":
            return "active";

          case "reward-tab-class":
            return "active showNavi";

          case "place-reward-range":
            if (!param) return true;
            return param <= 5000 && param >= 100;

          case "optional-place-reward-range":
            if (!param) return true;
            return param <= 5000 && param >= 100;

          case "compare-3reward-2reward":
            if (!$scope.contest.thirdPlaceReward) return true;
            return $scope.contest.thirdPlaceReward <= $scope.contest.secondPlaceReward;

          case "compare-2reward-1reward":
            if (!$scope.contest.secondPlaceReward) return true;
            return $scope.contest.secondPlaceReward <= $scope.contest.firstPlaceReward;
        }
      },

      nextState: function () {
        var request = angular.copy($scope.contest);
        request.lang = $translate.use();
        $anchorScroll();
        utils.sendNotification(jsonValue.notifications.loading);
        $('.submit-contest-content').find('button').addClass('disabled');
        $http.post("challenge/publish", request)
          .then(function (response) {
            var title = utils.toAscii($scope.contest.challengeName);
            $location.url(sprintf("/challenge-detail/%s-%s-id", title, response.data.challengeId));
          })
          .finally(function () {
            $('.submit-contest-content').find('button').removeClass('disabled');
            utils.sendNotification(jsonValue.notifications.hideLoadingBox);
          });
        return true;
      }
    },

    orderStates: ["challenge", "timeline", "reward"]
  }

  //var orderState = function (st) {
  //  var states = ["challenge", "timeline", "reward"];
  //  return states.indexOf(st);
  //}

  $scope.changeState = function (st, param) {
    var ignoreInvalidForm = param && param.ignoreInvalidForm;
    if (ignoreInvalidForm != true) {
      if (!st || ($scope.state && !$scope.state.status("is-form-valid"))) {
        return false;
      }
    }

    if ($.type(st) === "function") {
      return st();
    }

    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
    $scope.state.status("set-form-pristine");
    return true;
  }

  $scope.nextState = function () {
    $scope.changeState($scope.state.nextState);
  }

  $scope.toState = function (st) {
    var currentStateOrder = state.orderStates.indexOf($scope.state.name);
    var toStateOrder = state.orderStates.indexOf(st);
    var next = currentStateOrder < toStateOrder;
    var currentStatePristine = $scope.state.status("is-form-pristine")
    var index = currentStateOrder;
    var lastStep = false;
    while (!lastStep) {
      if (index == toStateOrder) {
        lastStep = true;
      }
      var stateName = state.orderStates[index];
      if (!$scope.changeState(stateName, {ignoreInvalidForm: !next && currentStatePristine})) {
        break;
      }
      index += (next ? 1 : -1);
    }
  }

  $scope.config = {
    reviewStyle: resourcesService.reviewStyleConfig,
    qualityIdea: resourcesService.qualityIdeaConfig
  }

  $scope.changeState(state.challenge);

  $scope.detectDeleteKey = function (e) {
    if (e.which == 8)
      return false;
  };
});