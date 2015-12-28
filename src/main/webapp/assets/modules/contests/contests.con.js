techlooper.controller('contestsController', function (apiService, $scope, jsonValue, $window, $translate, $filter,
                                                      utils, localStorageService, $route) {
  utils.sendNotification(jsonValue.notifications.loading);
  var joinChallenge = function() {
    if (localStorageService.get("joinNow")) {
      localStorageService.remove("joinNow");
      var firstName = localStorageService.get("firstName");
      var lastName = localStorageService.get("lastName");
      var email = localStorageService.get("email");//submitNow
      var contestId = localStorageService.get("joiningChallengeId") || localStorageService.get("submitNow");
      if (contestId) {
        email && apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
          .success(function (numberOfRegistrants) {
            var joinContests = localStorageService.get("joinContests") || "";
            joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
            if ($.inArray(contestId, joinContests) < 0) {
              joinContests.push(contestId);
            }

            localStorageService.set("joinContests", joinContests.join(","));

            if ($scope.contestDetail) {
              $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
              $scope.contestDetail.recalculateCurrentUserJoined();
            }
            else {
              $route.reload();
            }
          });
        localStorageService.remove("joiningChallengeId");
      }
    }
  }

  joinChallenge();

  $scope.contestTimeLeft = function (contest) {
    if (!contest.progress) return "";
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

    return "";
  }

  apiService.searchContests().success(function (contests) {
    utils.sortByDate(contests, "startDateTime");
    $scope.contestsList = contests;
  }).finally(function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  $scope.joinNow = function (challenge) {
    localStorageService.set("joiningChallengeId", challenge.challengeId);
    if (challenge.$isPublic) {
      apiService.joinNowByFB();
    }
    else if (challenge.$isInternal) {
      $scope.toggleJoinInternalForm(challenge);//TODO join internal challenge
      //localStorageService.set("priorFoot", $location.url());
      //localStorageService.set("lastFoot", $location.url());
      //localStorageService.set("joinNow", true);
      //joinChallenge();
    }
  }
});