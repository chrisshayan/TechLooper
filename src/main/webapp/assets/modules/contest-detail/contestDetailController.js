techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter, $timeout, resourcesService, $timeout) {
  utils.sendNotification(jsonValue.notifications.loading);
  $scope.selectedPhase = 0;
  var activePhaseIndex = 0;
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

  $scope.reviewPhase = function (index, phase, nextPhase) {
    var phaseName = phase ? phase.phase : jsonValue.challengePhase.getRegistration().enum;
    if (index && index > activePhaseIndex+1){
        return;
    }else{
      $('.feedback-loading').css('visibility', 'inherit');
      $scope.selectedPhase = index;
    }
    apiService.getChallengeRegistrantsByPhase(contestId, phaseName).success(function (data) {
      $scope.registrantPhase = data;

      switch (phaseName) {
        case "REGISTRATION":
          $scope.sortByRegistrationDate();
          break;

        default:
          $scope.sortBySubmissionDate();
          break;
      }

      var param = $location.search();
      if (param.a == "registrants") {
        var registrantTab = $('.nav-tabs a[href=".registrants"]');
        if (registrantTab) {
          registrantTab.tab('show');
        }
      }
    }).finally(function () {
      utils.sendNotification(jsonValue.notifications.loaded);
      $timeout(function () {
        $('.feedback-loading').css('visibility', 'hidden');
      }, 1200);
    });
  };

  $scope.sortByRegistrationDate = function () {
    $scope.sortByRegistrationDateType = $scope.sortByRegistrationDateType == "asc" ? "desc" : "asc";
    utils.sortByNumber($scope.registrantPhase, "registrantId", $scope.sortByRegistrationDateType);
  }

  $scope.sortBySubmissionDate = function () {
    $scope.sortByRegistrationDateType = $scope.sortByRegistrationDateType == "asc" ? "desc" : "asc";
    utils.sortByNumber($scope.registrantPhase, "challengeSubmissionId", $scope.sortBySubmissionDateType);
  }

  $scope.sortByScore = function () {
    $scope.sortByScoreType = $scope.sortByScoreType == "asc" ? "desc" : "asc";
    utils.sortByNumber($scope.registrantPhase, "savedTotalPoint", $scope.sortByScoreType);
  };

  $scope.failJoin = false;
  $scope.action = '';
  $scope.actionContent = '';
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
        failJoin = false;
        return !hasJoined;

      case "contest-in-progress":
        if (!$scope.contestDetail) return false;
        return ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);

      case "feedback-form":
        $scope.action = 'feedback';
        return $scope.action;
      case "disqualify-form":
        $scope.action = 'disqualify';
        return $scope.action;
      case "qualify-form":
        $scope.action = 'qualify';
        return $scope.action;
      case "review-history":
        $scope.action = 'review';
        return $scope.action;
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
        if ($scope.contestDetail) {
          $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
        }

        var joinContests = localStorageService.get("joinContests") || "";
        joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
        if ($.inArray(contestId, joinContests) < 0) {
          joinContests.push(contestId);
        }

        localStorageService.set("joinContests", joinContests.join(","));
        $filter("progress")($scope.contestDetail, "challenge");
        //$scope.filterContestant();
      })
      .error(function () {
        $scope.failJoin = true;
      });
  }

  apiService.getContestDetail(contestId)
    .success(function (data) {
      $scope.contestDetail = data;
      $filter("progress")($scope.contestDetail, "challenge");
      var idea = $scope.contestDetail.ideaSubmissionDateTime ? true : false,
        uiux = $scope.contestDetail.uxSubmissionDateTime ? true : false,
        prototype = $scope.contestDetail.prototypeSubmissionDateTime ? true : false;
      if (!idea || !uiux || !prototype) {
        if ((idea && uiux) || (idea && prototype) || (prototype && uiux)) {
          $scope.contestDetail.timeline = 4;
        }
        else if (((!idea && !uiux) && prototype) || ((!idea && !prototype) && uiux) || ((!prototype && !uiux) && idea)) {
          $scope.contestDetail.timeline = 3;
        }
        else {
          $scope.contestDetail.timeline = 2;
        }
      }
      else {
        $scope.contestDetail.timeline = 5;
      }
      $scope.getRegistrants(contestId);
    })
    .error(function () {$location.url("404");});

  $scope.getRegistrants = function (contestId) {
    apiService.getRegistrantFunnel(contestId)
      .success(function (data) {
        $scope.registrantFunnel = data;
        $.each($scope.registrantFunnel, function (i, item) {
          if (item.phase == $scope.contestDetail.currentPhase) {
            $scope.registrantFunnel.currentPosition = i;
          }
        });
        $scope.selectedPhase = $scope.registrantFunnel.currentPosition;
        activePhaseIndex = $scope.registrantFunnel.currentPosition;
      }).error(function () {
      console.log('error');
        utils.sendNotification(jsonValue.notifications.loaded);
    });
  }
  $scope.reviewPhase();
  $scope.fbShare = function () {
    ga("send", {
      hitType: "event",
      eventCategory: "facebookshare",
      eventAction: "click",
      eventLabel: "challengedetail"
    });
    utils.openFBShare("/shareChallenge/" + $translate.use() + "/" + contestId);
  }

  $scope.sortUsers = function (keyname) {
    $scope.sortKey = keyname;   //set the sortKey to the param passed
    $scope.reverse = !$scope.reverse; //if true make it false and vice versa
  };

  $scope.config = {
    registrantsFilter: resourcesService.registrantsFilterConfig,
    registrantsPhase: resourcesService.registrantsPhaseConfig
  };
  $scope.dateFrom = moment().add(-1, 'day').format('DD/MM/YYYY');
  $scope.dateTo = moment().format('DD/MM/YYYY');
  $('.registrants-date').find('.date').datepicker({
    autoclose: true,
    format: 'dd/mm/yyyy'
  });

  $scope.saveChallengeCriteria = function () {
    $scope.contestDetail.validate();
    if ($scope.contestDetail.totalWeight > 100 || $scope.contestDetail.$invalid) {
      return false;
    }

    $scope.contestDetail.saveCriteria();
  }

  $scope.$on("update-funnel", function(sc, registrant) {
    //console.log(registrant);
    $scope.getRegistrants(registrant.challengeId);
  });
});

