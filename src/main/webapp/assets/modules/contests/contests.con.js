techlooper.controller('contestsController', function (apiService, $scope, jsonValue) {
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
});