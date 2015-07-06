techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams, jsonValue) {

  var contestId = $routeParams.id;

  $scope.status = function(type) {
    switch (type) {
      case "able-to-join":
        if (!$scope.contestDetail) return false;
        var joinContests = localStorageService.get("joinContests") || "";
        var registerVnwUser = localStorageService.get("registerVnwUser") || "";
        var contestStatus = ($scope.contestDetail.progress == jsonValue.status.registration.translate) ||
          ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
        var hasNotJoined = joinContests.indexOf(contestId) < 0;
        return contestStatus && hasNotJoined;

      case "contest-in-progress":
        if (!$scope.contestDetail) return false;
        return ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
    }
  }

  $scope.joinNowByFB = function() {
    if (!$scope.status('able-to-join')) {
      return false;
    }

    localStorageService.set("lastFoot", $location.url());
    apiService.getFBLoginUrl().success(function(url) {
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
      .success(function(data) {
        console.log(data);
      });
  }

  apiService.getContestDetail(contestId).success(function(data) {
    console.log(data);
    $scope.contestDetail = data;
    //$scope.contestDetail.countDown = parseInt(moment().countdown($scope.formatDate($scope.contestDetail.startDateTime), countdown.DAYS, NaN, 2).toString());
    //$scope.contestDetail.status = moment($scope.contestDetail.startDateTime).toNow();
    //console.log($scope.contestDetail.status);
  });

  $scope.fbShare = function(url, title, descr, image, winWidth, winHeight) {
    var winTop = (screen.height / 2) - (winHeight / 2);
    var winLeft = (screen.width / 2) - (winWidth / 2);
    window.open('http://www.facebook.com/sharer.php?s=100&p[title]=' + title + '&p[summary]=' + descr + '&p[url]=' + url + '&p[images][0]=' + image, 'sharer', 'top=' + winTop + ',left=' + winLeft + ',toolbar=0,status=0,width=' + winWidth + ',height=' + winHeight);
  }

});

