techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter, $route) {
  //utils.sendNotification(jsonValue.notifications.loading);
  //$scope.currentPage = 1;
  //$scope.selectedPhase = 0;
  //var activePhaseIndex = 0;
  //var flagUpdate = false;
  var parts = $routeParams.id.split("-");
  var lastPart = parts.pop();
  if (parts.length < 2 || (lastPart !== "id")) {
    return $location.path("/");
  }

  var contestId = parts.pop();
  var title = parts.join("");
  if (utils.hasNonAsciiChar(title)) {
    title = utils.toAscii(title);
    return $location.url(sprintf("/challenge-detail/%s-%s-id", title, contestId));
  }

  //$scope.reviewPhase = function (index, phase, nextPhase) {
  //  var phaseName = phase ? phase.phase : jsonValue.challengePhase.getRegistration().enum;
  //  if (index && (index > activePhaseIndex + 1) && phase.phase !='WINNER') {
  //    return;
  //  }
  //  else {
  //    $('.feedback-loading').css('visibility', 'inherit');
  //    $scope.selectedPhase = index;
  //  }
  //
  //  delete $scope.sortByRegistrationDateType;
  //  delete $scope.sortBySubmissionDateType;
  //  delete $scope.sortByScoreType;
  //
  //  apiService.getChallengeRegistrantsByPhase(contestId, phaseName).success(function (data) {
  //    $scope.registrantPhase = data;
  //    $scope.contestDetail.recalculate(phaseName, $scope.registrantPhase);
  //    _.each($scope.registrantPhase, function (rgt) {rgt.recalculateWinner($scope.contestDetail);});
  //
  //    switch (phaseName) {
  //      case "REGISTRATION":
  //        $scope.sortByRegistrationDate();
  //        break;
  //
  //      case "WINNER":
  //        $scope.sortByScore();
  //        break;
  //
  //      default:
  //        $scope.sortBySubmissionDate();
  //        break;
  //    }
  //
  //    var param = $location.search();
  //    if (param.a == "registrants") {
  //      var registrantTab = $('.nav-tabs a[href=".registrants"]');
  //      if (registrantTab) {
  //        registrantTab.tab('show');
  //      }
  //    }
  //  }).finally(function () {
  //    utils.sendNotification(jsonValue.notifications.loaded);
  //    $timeout(function () {
  //      $('.feedback-loading').css('visibility', 'hidden');
  //    }, 1200);
  //  });
  //};

  //$scope.sortByRegistrationDate = function () {
  //  $scope.sortByRegistrationDateType = $scope.sortByRegistrationDateType == "desc" ? "asc" : "desc";
  //  delete $scope.sortBySubmissionType;
  //  utils.sortByNumber($scope.registrantPhase, "registrantId", $scope.sortByRegistrationDateType);
  //}
  //
  //$scope.sortBySubmissionDate = function () {
  //  $scope.sortBySubmissionDateType = $scope.sortBySubmissionDateType == "asc" ? "desc" : "asc";
  //  delete $scope.sortBySubmissionType;
  //  delete $scope.sortByScoreType;
  //  $scope.registrantPhase.sort(function (a, b) {
  //    var interval = $scope.sortBySubmissionDateType == "asc" ? 1 : -1;
  //    return interval * ((b.lastSubmission ? b.lastSubmission.challengeSubmissionId : 0) - (a.lastSubmission ? a.lastSubmission.challengeSubmissionId : 0));
  //  });
  //}
  //
  //$scope.sortByScore = function () {
  //  $scope.sortByScoreType = $scope.sortByScoreType == "desc" ? "asc" : "desc";
  //  delete $scope.sortBySubmissionType;
  //  delete $scope.sortBySubmissionDateType;
  //  utils.sortByNumber($scope.registrantPhase, "totalPoint", $scope.sortByScoreType);
  //};
  //
  //$scope.sortBySubmission = function () {
  //  $scope.sortBySubmissionType = $scope.sortBySubmissionType == "desc" ? "asc" : "desc";
  //  delete $scope.sortByScoreType;
  //  delete $scope.sortBySubmissionDateType;
  //  delete $scope.sortByRegistrationDateType
  //  utils.sortByArrayLength($scope.registrantPhase, "submissions", $scope.sortBySubmissionType);
  //};

  //$scope.failJoin = false;
  //$scope.action = '';
  //$scope.actionContent = '';
  $scope.status = function (type, id) {
    switch (type) {
      case "able-to-join":
        if (!$scope.contestDetail) return false;
        var joinContests = localStorageService.get("joinContests") || "";
        var email = localStorageService.get("email") || "";
        var contestInProgress = ($scope.contestDetail.progress.translate == jsonValue.status.registration.translate) ||
          ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
        var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
        return contestInProgress && !hasJoined;

      case "already-join":
        if (!$scope.contestDetail) return false;
        var joinContests = localStorageService.get("joinContests") || "";
        var email = localStorageService.get("email") || "";
        var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
        //failJoin = false;
        return !hasJoined;

      case "contest-in-progress":
        if (!$scope.contestDetail) return false;
        return ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);

      //case "feedback-form":
      //  $scope.action = 'feedback';
      //  return $scope.action;
      //case "disqualify-form":
      //  $scope.action = 'disqualify';
      //  return $scope.action;
      //case "qualify-form":
      //  $scope.action = 'qualify';
      //  return $scope.action;
      //case "review-history":
      //  $scope.action = 'review';
      //  return $scope.action;
    }
  }

  $scope.contestTimeLeft = function (contest) {
    if (contest) {
      switch (contest.progress.translate) {
        case jsonValue.status.progress.translate:
          return $filter("countdown")(contest.submissionDateTime);

        case jsonValue.status.notStarted.translate:
          return $filter("countdown")(contest.startDateTime);

        case jsonValue.status.registration.translate:
          return $filter("countdown")(contest.registrationDateTime);

        case jsonValue.status.closed.translate:
          return contest.submissionDateTime;
      }
    }
    return "";
  }

  $scope.joinNowByFB = function () {
    if (!$scope.status('able-to-join')) {
      return false;
    }
    apiService.joinNowByFB();
  }

  if (localStorageService.get("joinNow")) {
    localStorageService.remove("joinNow");
    var firstName = localStorageService.get("firstName");
    var lastName = localStorageService.get("lastName");
    var email = localStorageService.get("email");
    apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
      .success(function (numberOfRegistrants) {
        //if ($scope.contestDetail) {
        //  $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
        //}

        var joinContests = localStorageService.get("joinContests") || "";
        joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
        if ($.inArray(contestId, joinContests) < 0) {
          joinContests.push(contestId);
        }

        localStorageService.set("joinContests", joinContests.join(","));
        //$filter("progress")($scope.contestDetail, "challenge");
        //$scope.filterContestant();
        $location.search({});
        $route.reload();
      })
      .error(function () {
        $scope.failJoin = true;
      });
  }

  $scope.refreshChallengeDetail = function() {
    apiService.getContestDetail(contestId)
      .success(function (data) {
        $scope.contestDetail = data;
        $filter("progress")($scope.contestDetail, "challenge");
        //$scope.contestDetail.refreshRegistrants();
        //var idea = $scope.contestDetail.ideaSubmissionDateTime ? true : false,
        //  uiux = $scope.contestDetail.uxSubmissionDateTime ? true : false,
        //  prototype = $scope.contestDetail.prototypeSubmissionDateTime ? true : false;
        //if (!idea || !uiux || !prototype) {
        //  if ((idea && uiux) || (idea && prototype) || (prototype && uiux)) {
        //    $scope.contestDetail.timeline = 4;
        //  }
        //  else if (((!idea && !uiux) && prototype) || ((!idea && !prototype) && uiux) || ((!prototype && !uiux) && idea)) {
        //    $scope.contestDetail.timeline = 3;
        //  }
        //  else {
        //    $scope.contestDetail.timeline = 2;
        //  }
        //}
        //else {
        //  $scope.contestDetail.timeline = 5;
        //}
        //$scope.getRegistrants(contestId);
      })
      .error(function () {$location.url("404");});
  }

  $scope.refreshChallengeDetail();

  //$scope.getRegistrants = function () {
  //  apiService.getChallengeRegistrantsByPhase($scope.contestDetail.challengeId, $scope.contestDetail.selectedPhase.$phaseConfig.enum)
  //    .success(function (registrants) {
  //      $scope.registrants = registrants;
  //      $scope.contestDetail.recalculateRegistrants($scope.registrants);
  //
  //      //$.each($scope.registrantFunnel, function (i, item) {
  //      //  if (item.phase == $scope.contestDetail.currentPhase) {
  //      //    $scope.registrantFunnel.currentPosition = i;
  //      //  }
  //      //});
  //      ////$scope.selectedPhase = $scope.registrantFunnel.currentPosition;
  //      //if (flagUpdate == false) {
  //      //  $scope.selectedPhase;
  //      //  var activePhaseName = '';
  //      //  $.each($scope.registrantFunnel, function(i, value){
  //      //    if($scope.selectedPhase == i){
  //      //      activePhaseName = value.phase;
  //      //    }
  //      //  });
  //      //  $scope.reviewPhase($scope.selectedPhase, activePhaseName);
  //      //}
  //      //else {
  //      //  activePhaseIndex = $scope.registrantFunnel.currentPosition;
  //      //  $scope.reviewPhase($scope.registrantFunnel.currentPosition, {phase: $scope.contestDetail.currentPhase});
  //      //}
  //      utils.sendNotification(jsonValue.notifications.loaded);
  //    }).error(function () {
  //    utils.sendNotification(jsonValue.notifications.loaded);
  //  });
  //};

  //$scope.resetActivePhase = function () {
  //  flagUpdate = true;
  //  $scope.reviewPhase($scope.registrantFunnel.currentPosition, {phase: $scope.contestDetail.currentPhase});
  //};

  $scope.fbShare = function () {
    ga("send", {
      hitType: "event",
      eventCategory: "facebookshare",
      eventAction: "click",
      eventLabel: "challengedetail"
    });
    utils.openFBShare("/shareChallenge/" + $translate.use() + "/" + contestId);
  }

  //$scope.sortUsers = function (keyname) {
  //  $scope.sortKey = keyname;   //set the sortKey to the param passed
  //  $scope.reverse = !$scope.reverse; //if true make it false and vice versa
  //};

  //$scope.config = {
  //  registrantsFilter: resourcesService.registrantsFilterConfig,
  //  registrantsPhase: resourcesService.registrantsPhaseConfig
  //};

  //$scope.dateFrom = moment().add(-1, 'day').format('DD/MM/YYYY');
  //$scope.dateTo = moment().format('DD/MM/YYYY');
  //$('.registrants-date').find('.date').datepicker({
  //  autoclose: true,
  //  format: 'dd/mm/yyyy'
  //});

  //$scope.saveChallengeCriteria = function () {
  //  $scope.contestDetail.validate();
  //  if ($scope.contestDetail.totalWeight > 100 || $scope.contestDetail.$invalid) {
  //    return false;
  //  }
  //
  //  $scope.contestDetail.saveCriteria();
  //}

  //$scope.$on("update-funnel", function (sc, registrant) {
  //  flagUpdate = false;
  //  $scope.getRegistrants(registrant.challengeId, flagUpdate);
  //});

  //$scope.$on("saveRegistrantCriteriaSuccessful", function (sc, registrant) {
  //  $scope.contestDetail.recalculateStateWinners($scope.registrantPhase);
  //});
  //
  //$scope.$on("success-submission-challenge", function (sc, registrant) {
  //  $scope.getRegistrants(registrant.challengeId);
  //});

  //$scope.$on("challenge-selected-phase-change", function (e, rt) {});
  //$scope.$on("on-qualified", function (e, rt) {rt.hideActionView();});
  //$scope.$on("on-disqualified", function (e, rt) {rt.hideActionView();});
  $scope.$on("before-getting-registrants", function (e, challengeDetail) {
    $('.feedback-loading').css('visibility', 'inherit');
  });

  $scope.$on("after-getting-registrants", function (e, challengeDetail) {
    $('.feedback-loading').css('visibility', 'hidden');
  });

});

