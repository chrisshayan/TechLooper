techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter) {

  var parts = $routeParams.id.split("-");
  var lastPart = parts.pop();
  if (parts.length < 2 || (lastPart !== "id")) {
    return $location.path("/");
  }
  var contestId = parts.pop();
  var title = parts.join("");
  if (utils.hasNonAsciiChar(title)) {
    title = utils.toAscii(title);
    return $location.url(sprintf("/contest-detail/%s-%s-id", title, contestId));
  }

  $scope.status = function (type) {
    switch (type) {
      case "able-to-join":
        if (!$scope.contestDetail) return false;
        var joinContests = localStorageService.get("joinContests") || "";
        var registerVnwUser = localStorageService.get("registerVnwUser") || "";
        var contestStatus = ($scope.contestDetail.progress == jsonValue.status.registration.translate) ||
          ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
        var hasJoined = (joinContests.indexOf(contestId) >= 0) && (registerVnwUser.length > 0);
        return contestStatus && !hasJoined;

      case "contest-in-progress":
        if (!$scope.contestDetail) return false;
        return ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
    }
  }

  $scope.joinNowByFB = function () {
    if (!$scope.status('able-to-join')) {
      return false;
    }

    localStorageService.set("lastFoot", $location.url());
    apiService.getFBLoginUrl().success(function (url) {
      var joinContests = localStorageService.get("joinContests") || "";
      if (joinContests.indexOf(contestId) < 0) {
        joinContests += joinContests.length > 0 ? "," + contestId : contestId;
      }
      localStorageService.set("joinContests", joinContests);
      localStorageService.set("lastFoot", $location.url());
      localStorageService.set("joinNow", true);
      window.location = url;
    });
  }

  if (localStorageService.get("joinNow")) {
    localStorageService.remove("joinNow")
    apiService.joinContest(contestId, localStorageService.get("registerVnwUser"))
      .success(function (numberOfRegistrants) {
        if ($scope.contestDetail) {
          $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
        }
      });
  }

  apiService.getContestDetail(contestId).success(function (data) {
    $scope.contestDetail = data;
    $filter("progress")($scope.contestDetail, "challenge");
  });

  $scope.fbShare = function () {
    utils.openFBShare("/shareChallenge/" + $translate.use() + "/" + contestId);
  }

});

