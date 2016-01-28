techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location,
                                                        securityService, $translate, apiService, $route, $rootScope) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinChallenge = function () {
    //if (localStorageService.get("joinNow")) {
    var contestId = localStorageService.get("joiningChallengeId");// || localStorageService.get("submitNow");
    var joinNowInternalChallenge = localStorageService.get("joinNowInternalChallenge");
    var firstName = localStorageService.get("firstName");
    var lastName = localStorageService.get("lastName");
    var email = localStorageService.get("email");//submitNow
    //var savedDraftRegistrant = localStorageService.get("savedDraftRegistrant");
    if (contestId && !joinNowInternalChallenge) {
      //console.log(joinNowInternalChallenge);//saveDraftRegistrant
      email && apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
        .success(function (numberOfRegistrants) {
          var joinContests = localStorageService.get("joinContests") || "";
          joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
          if ($.inArray(contestId, joinContests) < 0) {
            joinContests.push(contestId);
          }

          localStorageService.set("joinContests", joinContests.join(","));

          //if ($scope.contestDetail) {
          //  $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
          //  $scope.contestDetail.recalculateCurrentUserJoined();
          //}
          //else {
          //  $route.reload();
          //}
          localStorageService.remove("failedJoin");
          $route.reload();
        })
        .error(function () {
          localStorageService.set("failedJoin", true);
        })
        .finally(function () {
          $route.reload();
        });
      localStorageService.remove("joinNow");
      //localStorageService.remove("joiningChallengeId");
      localStorageService.remove("joinNowInternalChallenge");
    }
    else if (contestId && joinNowInternalChallenge) {
      //if (savedDraftRegistrant) {
      //  return securityService.routeByRole();
      //}
      //else {
      //  email && apiService.saveDraftRegistrant(contestId, firstName, lastName, email, $translate.use())
      //    .success(function (draft) {
      //      localStorageService.set("savedDraftRegistrant", draft.registrantId);
      //    })
      //    .finally(function () {
      //      $route.reload();
      //    });
      //  localStorageService.remove("joinNow");
      //  localStorageService.remove("joiningChallengeId");
      //  localStorageService.remove("joinNowInternalChallenge");
      //}
      localStorageService.remove("joinNow");
      //localStorageService.remove("joiningChallengeId");
      localStorageService.remove("joinNowInternalChallenge");
      $route.reload();
    }
    //}

  };

  var joinNow = localStorageService.get("joinNow");
  //var localStorageService = localStorageService.get("localStorageService");
  //console.log(localStorageService);
  if (joinNow == true) {
    return joinChallenge();

    //return securityService.routeByRole();
  }

  //var param = $location.search();
  //if (!$.isEmptyObject(param)) {
  //  switch (param.action) {
  //    case "challenge-detail":
  //      localStorageService.set("challengePhaseName", param.name);
  //      $location.url("/home");
  //      break;
  //  }
  //}

  var param = $location.search();
  if ($.isEmptyObject(param)) {
    return $location.url("/home");
  }

  securityService.routeByRole();
});