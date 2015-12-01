techlooper.controller("postContestController", function ($scope, $http, jsonValue, $translate, $location, utils,
                                                         resourcesService, $anchorScroll, apiService) {

  var param = $location.search();
  if (param.id && (param.a == "edit")) {
    apiService.findChallengeById(param.id).success(function (data) {
      $scope.contest = data;
      $scope.contest.fromServer = true;
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
            return $.isEmptyObject($scope.challengeForm.$error);//!$scope.challengeForm.$invalid;

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
            $scope.timelineForm.$setSubmitted();
            return $.isEmptyObject($scope.timelineForm.$error);

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
            return $.isEmptyObject($scope.rewardForm.$error);

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
            localStorage.postedChallenge = "justPosted";
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
  };

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
  };

  $scope.nextState = function () {
    $scope.changeState($scope.state.nextState);
  };

  $scope.toState = function (st) {
    var currentStateOrder = state.orderStates.indexOf($scope.state.name);
    var toStateOrder = state.orderStates.indexOf(st);
    if (currentStateOrder == toStateOrder) return;

    var next = currentStateOrder < toStateOrder;
    if (!next) return $scope.changeState(st, {ignoreInvalidForm: true});

    var currentStatePristine = $scope.state.status("is-form-pristine")
    var index = currentStateOrder;
    var lastStep = false;
    while (!lastStep) {
      var stateName = state.orderStates[index];
      if (!$scope.changeState(stateName, {ignoreInvalidForm: !next && currentStatePristine})) {
        break;
      }
      index += (next ? 1 : -1);
      if (index == toStateOrder + 1) {
        lastStep = true;
      }
    }
  };

  $scope.config = {
    reviewStyle: resourcesService.reviewStyleConfig,
    qualityIdea: resourcesService.qualityIdeaConfig
  };

  $scope.changeState(state.challenge);

  $scope.detectDeleteKey = function (e) {
    if (e.which == 8) return false;
  };

  //$scope.contest = {};

  var gtDate = function (modelValue, contestProperty) {
    if (!modelValue) return true;

    var currentDate = moment(modelValue, jsonValue.dateFormat);
    var compareDate = moment($scope.contest[contestProperty], jsonValue.dateFormat);
    if (!compareDate.isValid()) return false;
    return currentDate.isAfter(compareDate, 'day');
  };

  // timeline validators
  $scope.$watch(function () {
    if (!$scope.contest) return [];
    return [
      $scope.contest.startDate, $scope.contest.registrationDate,
      $scope.contest.ideaSubmissionDate, $scope.contest.uxSubmissionDate,
      $scope.contest.prototypeSubmissionDate, $scope.contest.submissionDate,
      $scope.ideaChecked, $scope.uiuxChecked, $scope.prototypeChecked
    ];
  }, function () {

    //if (!$scope.timelineForm) return;
    //if (!$scope.contest) return;

    //if (!$scope.ideaChecked) delete $scope.contest.ideaSubmissionDate;
    //if (!$scope.uiuxChecked) delete $scope.contest.uxSubmissionDate;
    //if (!$scope.prototypeChecked) delete $scope.contest.prototypeSubmissionDate;
    //
    //var timeline = [
    //  {name: "startDate", value: moment().add(4, 'weeks'), validatorName: "in4w"},
    //  {name: "startDate", value: moment($scope.contest.startDate, jsonValue.dateFormat)},
    //  {name: "registrationDate", value: moment($scope.contest.registrationDate, jsonValue.dateFormat)},
    //  {name: "ideaSubmissionDate", value: moment($scope.contest.ideaSubmissionDate, jsonValue.dateFormat)},
    //  {name: "uxSubmissionDate", value: moment($scope.contest.uxSubmissionDate, jsonValue.dateFormat), dependFrom: "registrationDate"},
    //  {name: "prototypeSubmissionDate", value: moment($scope.contest.prototypeSubmissionDate, jsonValue.dateFormat), dependFrom: "registrationDate"},
    //  {name: "submissionDate", value: moment($scope.contest.submissionDate, jsonValue.dateFormat), dependFrom: "registrationDate"}
    //];
    //
    ////var last = timeline[0];
    //var validators = [];
    //for (var i = 1; i < timeline.length; ++i) {
    //  var current = timeline[i];
    //
    //  $.each(validators, function(i, validator) {//reset all validators
    //    $scope.timelineForm[current.name].$setValidity(validator, true);
    //  });
    //
    //  if (!current.value.isValid()) continue;
    //
    //  var before = timeline[i-1];
    //  var validatorName = before.validatorName || ("gt" + before.name.charAt(0).toUpperCase() + before.name.substr(1));
    //  validators.push(validatorName);
    //  var valid = before.value.isValid() ? current.value.isAfter(before.value) : false;
    //  console.log(valid);
    //  $scope.timelineForm[current.name].$setValidity(validatorName, valid);
    //  console.log($scope.timelineForm[current.name]);
    //}


    if (!$scope.contest) return;

    var currentDate = undefined;
    var compareDate = undefined;
    var valid = undefined;

    //start-date
    currentDate =  moment($scope.contest.startDate, jsonValue.dateFormat);
    if (currentDate.isValid()) {
      compareDate = moment().add(4, 'weeks');
      valid = currentDate.isBetween(moment(), compareDate, 'day') || currentDate.isSame(moment(), "day");
      $scope.timelineForm.startDate.$setValidity("in4w", valid);
    }

    //registration-date
    valid = gtDate($scope.contest.registrationDate, "startDate");
    $scope.timelineForm && $scope.timelineForm.registrationDate.$setValidity("gtStartDate", valid);

    //idea-date
    if ($scope.contest.fromServer && $scope.contest.ideaSubmissionDate) $scope.ideaChecked = true;
    valid = !$scope.ideaChecked || ($scope.ideaChecked && gtDate($scope.contest.ideaSubmissionDate, "registrationDate"));
    $scope.timelineForm && $scope.timelineForm.ideaSubmissionDate.$setValidity("gtRegistrationDate", valid);

    //ux-date
    if ($scope.contest.fromServer && $scope.contest.uxSubmissionDate) $scope.uiuxChecked = true;
    $scope.timelineForm && $scope.timelineForm.uxSubmissionDate.$setValidity("gtRegistrationDate", true);
    $scope.timelineForm && $scope.timelineForm.uxSubmissionDate.$setValidity("gtIdeaSubmissionDate", true);
    compareDate = $scope.ideaChecked ? "ideaSubmissionDate" : "registrationDate";
    valid = !$scope.uiuxChecked || ($scope.uiuxChecked && gtDate($scope.contest.uxSubmissionDate, compareDate));
    compareDate = compareDate.charAt(0).toUpperCase() + compareDate.substr(1);
    $scope.timelineForm && $scope.timelineForm.uxSubmissionDate.$setValidity("gt" + compareDate, valid);

    //prototype-date
    if ($scope.contest.fromServer && $scope.contest.prototypeSubmissionDate) $scope.prototypeChecked = true;
    $scope.timelineForm && $scope.timelineForm.prototypeSubmissionDate.$setValidity("gtRegistrationDate", true);
    $scope.timelineForm && $scope.timelineForm.prototypeSubmissionDate.$setValidity("gtUxSubmissionDate", true);
    $scope.timelineForm && $scope.timelineForm.prototypeSubmissionDate.$setValidity("gtIdeaSubmissionDate", true);
    compareDate = $scope.uiuxChecked ? "uxSubmissionDate" : ($scope.ideaChecked ? "ideaSubmissionDate" : "registrationDate");
    valid = !$scope.prototypeChecked || ($scope.prototypeChecked && gtDate($scope.contest.prototypeSubmissionDate, compareDate));
    compareDate = compareDate.charAt(0).toUpperCase() + compareDate.substr(1);
    $scope.timelineForm && $scope.timelineForm.prototypeSubmissionDate.$setValidity("gt" + compareDate, valid);

    // submission-date
    $scope.timelineForm && $scope.timelineForm.submissionDate.$setValidity("gtRegistrationDate", true);
    $scope.timelineForm && $scope.timelineForm.submissionDate.$setValidity("gtIdeaSubmissionDate", true);
    $scope.timelineForm && $scope.timelineForm.submissionDate.$setValidity("gtUxSubmissionDate", true);
    $scope.timelineForm && $scope.timelineForm.submissionDate.$setValidity("gtPrototypeSubmissionDate", true);
    compareDate = $scope.prototypeChecked ? "prototypeSubmissionDate" : ($scope.uiuxChecked ? "uxSubmissionDate" : ($scope.ideaChecked ? "ideaSubmissionDate" : "registrationDate"));
    valid = gtDate($scope.contest.submissionDate, compareDate);
    compareDate = compareDate.charAt(0).toUpperCase() + compareDate.substr(1);
    $scope.timelineForm && $scope.timelineForm.submissionDate.$setValidity("gt" + compareDate, valid);

    if (!$scope.ideaChecked) delete $scope.contest.ideaSubmissionDate;
    if (!$scope.uiuxChecked) delete $scope.contest.uxSubmissionDate;
    if (!$scope.prototypeChecked) delete $scope.contest.prototypeSubmissionDate;

    delete $scope.contest.fromServer;
  }, true);
});