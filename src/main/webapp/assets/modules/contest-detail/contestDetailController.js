techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter, $timeout) {

  utils.sendNotification(jsonValue.notifications.loading);
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
    })
    .error(function () {$location.url("404");});

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

  apiService.getChallengeRegistrants(contestId)
    .success(function (registrants) {
      $scope.registrants = registrants;
      $scope.sortByStartDate();
      var param = $location.search();
      if (param.a == "registrants" && registrants.length) {
        $('.nav-tabs a[href=".registrants"]').tab('show');
      }
    }).finally(function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });
  $scope.sortByScore = function () {
    delete $scope.sortStartDate;
    $scope.sortScore = $scope.sortScore || "asc";
    $scope.sortScore = $(["asc", "desc"]).not([$scope.sortScore]).get()[0];
    utils.sortByNumber($scope.registrants, "score", $scope.sortScore);
  }

  $scope.sortByStartDate = function () {
    delete $scope.sortScore;
    $scope.sortStartDate = $scope.sortStartDate || "asc";
    $scope.sortStartDate = $(["asc", "desc"]).not([$scope.sortStartDate]).get()[0];
    utils.sortByNumber($scope.registrants, "registrantId", $scope.sortStartDate);
  }
  $scope.updateScore = function (registrant, $event) {
    $($event.currentTarget).addClass('green');
    apiService.saveChallengeRegistrant(registrant)
      .success(function (rt) {
        registrant.score = rt.score;
      }).finally(function () {
      $timeout(function () {
        $($event.currentTarget).removeClass('green');
      }, 1000);
    });
  }

  $scope.showSubmitForm = function () {
    var subForm = $('.submit-phase-contest');
    if (subForm.hasClass('show')) {
      subForm.removeClass('show');
    }
    else {
      subForm.addClass('show');
    }
  }

  //$scope.showActionForm = function(id){
  //  $('.action-content').removeClass('show');
  //  var parent = $('#id-'+id);
  //  var div = parent.find('.action-content');
  //  if(div.hasClass('show')){
  //    div.removeClass('show');
  //  }else{
  //    div.addClass('show');
  //  }
  //}
});

