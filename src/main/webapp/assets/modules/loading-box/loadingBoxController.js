techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location,
                                                        securityService, $translate, apiService, $route, joinAnythingService) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  //var contestId = localStorageService.get("joiningChallengeId");
  ////var joinNowInternalChallenge = localStorageService.get("joinNowInternalChallenge");
  //var firstName = localStorageService.get("firstName");
  //var lastName = localStorageService.get("lastName");
  //var email = localStorageService.get("email");
  //var internalEmail = localStorageService.get("internalEmail");
  //var passcode = localStorageService.get("passcode");
  //
  //var successfulJoinChallenge = function (joiningRegistrant) {
  //  var joinContests = localStorageService.get("joinContests") || "";
  //  joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
  //  if ($.inArray(contestId, joinContests) < 0) {
  //    joinContests.push(contestId);
  //  }
  //  localStorageService.set("joinContests", joinContests.join(","));
  //  localStorageService.remove("failedJoin");
  //}
  //
  //var joinChallenge = function () {
  //  //if (joinNowInternalChallenge) {
  //  //
  //  //}
  //  //else {
  //  //  apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
  //  //    .success(successfulJoinChallenge)
  //  //    .error(function () {
  //  //      localStorageService.set("failedJoin", true);
  //  //    })
  //  //    .finally(function () {
  //  //      $route.reload();
  //  //    });
  //  //}
  //  apiService.joinContest(contestId, firstName, lastName, email, $translate.use(), internalEmail, passcode)
  //    .success(successfulJoinChallenge)
  //    .error(function () {
  //      localStorageService.set("failedJoin", true);
  //    })
  //    .finally(function () {
  //      $route.reload();
  //    });
  //
  //  //if (contestId && !joinNowInternalChallenge) {
  //  //  email && apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
  //  //    .success(successfulJoinChallenge)
  //  //    .error(function () {
  //  //      localStorageService.set("failedJoin", true);
  //  //    })
  //  //    .finally(function () {
  //  //      $route.reload();
  //  //    });
  //  //  localStorageService.remove("joinNow");
  //  //  localStorageService.remove("joinNowInternalChallenge");
  //  //}
  //  //else if (contestId && joinNowInternalChallenge) {
  //  //  apiService.joinedChallenge(email, contestId)
  //  //    .success(function(joined) {
  //  //      if (joined == "true") {
  //  //        var joinContests = localStorageService.get("joinContests") || "";
  //  //        joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
  //  //        if ($.inArray(contestId, joinContests) < 0) {
  //  //          joinContests.push(contestId);
  //  //        }
  //  //        localStorageService.set("joinContests", joinContests.join(","));
  //  //        localStorageService.remove("failedJoin");
  //  //        $route.reload();
  //  //      }
  //  //      else {
  //  //        localStorageService.remove("joinNow");
  //  //        localStorageService.remove("joinNowInternalChallenge");
  //  //        $route.reload();
  //  //      }
  //  //    });
  //  //}
  //};

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    return joinAnythingService.joinChallenge();
  }

  var param = $location.search();
  if ($.isEmptyObject(param)) {
    return $location.url("/home");
  }

  securityService.routeByRole();
});