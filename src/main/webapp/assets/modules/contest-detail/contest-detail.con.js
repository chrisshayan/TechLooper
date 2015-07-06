techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams) {
  //$scope.showDeadlineInfo = function(){
  //  $scope.toggle = !$scope.toggle;
  //}
  
  $scope.countDownDay = parseInt(moment().countdown("07/10/2015", countdown.DAYS, NaN, 2).toString());

  var contestId = $routeParams.id;

  $scope.status = function(type) {
    switch (type) {
      case "able-to-join":
        var joinContests = localStorageService.get("joinContests") || "";
        var registerVnwUser = localStorageService.get("registerVnwUser") || "";
        return joinContests.indexOf(contestId) < 0 || registerVnwUser.length == 0;
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
      window.location = url;
    });
  }

  apiService.getContestDetail(contestId).success(function(data) {
    //TODO bind data
    console.log(data);
    $scope.contestDetail = data;
  });
});

