techlooper.controller('contestsController', function (apiService, $scope, jsonValue, $window, $translate, $filter,
                                                      utils, localStorageService) {
  utils.sendNotification(jsonValue.notifications.loading);

  if (localStorageService.get("joinNow")) {
    localStorageService.remove("joinNow");
    var firstName = localStorageService.get("firstName");
    var lastName = localStorageService.get("lastName");
    var email = localStorageService.get("email");
    var contestId = localStorageService.get("joiningChallengeId");
    if (contestId) {
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
        });
      localStorageService.remove("joiningChallengeId");
    }
  }

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
    //contests.sort(sortByStartDate);
    utils.sortByDate(contests, "startDateTime");
    $scope.contestsList = contests;
  }).finally(function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  $scope.joinNowByFB = function (challenge) {
    localStorageService.set("joiningChallengeId", challenge.challengeId);
    apiService.joinNowByFB();
  }
});