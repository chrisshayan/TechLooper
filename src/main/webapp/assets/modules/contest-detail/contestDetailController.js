techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter, $route, $timeout, localStorageService, $rootScope) {
  if (localStorageService.get("postChallenge") == true) {
    localStorageService.remove("postChallenge");
    $scope.postChallengeSuccess = true;
  }
  var parts = $routeParams.id.split("-");
  var lastPart = parts.pop();
  if (parts.length < 2 || (lastPart !== "id")) {
    return $location.path("/");
  }
  $scope.openEvaluationCriteria = function () {
    $("a[href='.evaluation-criteria']").tab('show');
  }
  var contestId = parts.pop();
  var title = parts.join("");
  if (utils.hasNonAsciiChar(title)) {
    title = utils.toAscii(title);
    return $location.url(sprintf("/challenge-detail/%s-%s-id", title, contestId));
  }

  //$scope.status = function (type, id) {
  //  var joinContests = localStorageService.get("joinContests") || "";
  //  var email = localStorageService.get("email") || "";
  //  var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
  //
  //  switch (type) {
  //    case "able-to-join":
  //      if (!$scope.contestDetail) return false;
  //      var contestInProgress = ($scope.contestDetail.progress.translate == jsonValue.status.registration.translate) ||
  //        ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
  //      return contestInProgress && !hasJoined;
  //
  //    //case "already-join":
  //    //  if (!$scope.contestDetail) return false;
  //    //  return !hasJoined;
  //
  //    case "contest-in-progress":
  //      if (!$scope.contestDetail) return false;
  //      return ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
  //  }
  //}

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
    if (!$scope.contestDetail.$currentState.isJoinable) {
      return false;
    }
    apiService.joinNowByFB();
  }

  if (localStorageService.get("joinNow")) {
    localStorageService.remove("joinNow");
    var firstName = localStorageService.get("firstName");
    var lastName = localStorageService.get("lastName");
    var email = localStorageService.get("email");
    email && apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
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

  $scope.refreshChallengeDetail = function () {
    apiService.getContestDetail(contestId)
      .success(function (data) {
        $scope.contestDetail = data;
        //$filter("progress")($scope.contestDetail, "challenge");
        $scope.contestDetail.setSelectedPhase($location.search().toPhase);
        $scope.$emit("challenge-detail-ready");
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
    if (!_.isEmpty(challengeDetail.$error)) {
      return $location.search({});
    }
    $('.feedback-loading').css('visibility', 'hidden');
  });
});