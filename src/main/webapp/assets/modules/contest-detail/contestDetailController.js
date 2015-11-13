techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter, $route) {
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

  //$scope.sortByRegistrationDate = function () {
  //  $scope.sortByRegistrationDateType = $scope.sortByRegistrationDateType == "desc" ? "asc" : "desc";
  //  delete $scope.sortBySubmissionType;
  //  utils.sortByNumber($scope.contestDetail.$registrants, "registrantId", $scope.sortByRegistrationDateType);
  //}

  //$scope.sortBySubmissionDate = function () {
  //  $scope.sortBySubmissionDateType = $scope.sortBySubmissionDateType == "asc" ? "desc" : "asc";
  //  delete $scope.sortBySubmissionType;
  //  delete $scope.sortByScoreType;
  //  $scope.contestDetail.$registrants.sort(function (a, b) {
  //    var interval = $scope.sortBySubmissionDateType == "asc" ? 1 : -1;
  //    return interval * ((b.lastSubmission ? b.lastSubmission.challengeSubmissionId : 0) - (a.lastSubmission ? a.lastSubmission.challengeSubmissionId : 0));
  //  });
  //}

  //$scope.sortByScore = function () {
  //  $scope.sortByScoreType = $scope.sortByScoreType == "desc" ? "asc" : "desc";
  //  delete $scope.sortBySubmissionType;
  //  delete $scope.sortBySubmissionDateType;
  //  utils.sortByNumber($scope.contestDetail.$registrants, "totalPoint", $scope.sortByScoreType);
  //};

  //$scope.sortBySubmission = function () {
  //  $scope.sortBySubmissionType = $scope.sortBySubmissionType == "desc" ? "asc" : "desc";
  //  delete $scope.sortByScoreType;
  //  delete $scope.sortBySubmissionDateType;
  //  delete $scope.sortByRegistrationDateType
  //  utils.sortByArrayLength($scope.contestDetail.$registrants, "submissions", $scope.sortBySubmissionType);
  //};

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

        var joinContests = localStorageService.get("joinContests") || "";
        joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
        if ($.inArray(contestId, joinContests) < 0) {
          joinContests.push(contestId);
        }

        localStorageService.set("joinContests", joinContests.join(","));
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
      })
      .error(function () {$location.url("404");});
  }

  $scope.refreshChallengeDetail();

  $scope.fbShare = function () {
    ga("send", {
      hitType: "event",
      eventCategory: "facebookshare",
      eventAction: "click",
      eventLabel: "challengedetail"
    });
    utils.openFBShare("/shareChallenge/" + $translate.use() + "/" + contestId);
  }

  $scope.$on("before-getting-registrants", function (e, challengeDetail) {
    $('.feedback-loading').css('visibility', 'inherit');
  });

  $scope.$on("after-getting-registrants", function (e, challengeDetail) {
    $('.feedback-loading').css('visibility', 'hidden');
  });
});

