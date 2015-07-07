techlooper.controller('contestsController', function (apiService, $scope, jsonValue, $window) {
  $scope.status = function (type, contest) {
    switch (type) {
      case "contest-in-progress":
        if (!contest) return false;
        return (contest.progress.translate == jsonValue.status.progress.translate);
    }
  }

  apiService.searchContests().success(function(contests) {
    $scope.contestsList = contests;
  });

  $scope.openNewTab = function(contest) {//{{contest.challengeName}}{{contest.challengeId}}
    $window.open(sprintf(baseUrl + "/#/contest-detail/%s-%s-id", contest.challengeName, contest.challengeId), '_blank');
  }
});