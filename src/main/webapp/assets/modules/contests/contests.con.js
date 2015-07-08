techlooper.controller('contestsController', function (apiService, $scope, jsonValue, $window, $translate, $filter) {
  //$scope.status = function (type, contest) {
  //  switch (type) {
  //    case "contest-in-progress":
  //      if (!contest) return false;
  //      return (contest.progress.translate == jsonValue.status.progress.translate);
  //  }
  //}


  $scope.contestTimeLeft = function(contest) {


    switch (contest.progress.translate) {
      case jsonValue.status.progress.translate:
        return $filter("countdown")(contest.submissionDateTime);

      case jsonValue.status.notStarted.translate:
        console.log($filter("countdown")(contest.startDateTime));
        return $filter("countdown")(contest.startDateTime);

      case jsonValue.status.registration.translate:
        return $filter("countdown")(contest.registrationDateTime);

      case jsonValue.status.closed.translate:
        return contest.submissionDateTime;
    }

    return "";
  }

  apiService.searchContests().success(function(contests) {
    $scope.contestsList = contests;
  });

});