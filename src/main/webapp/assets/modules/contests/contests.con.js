techlooper.controller('contestsController', function (apiService, $scope, jsonValue, $window, $translate, $filter, utils) {
  utils.sendNotification(jsonValue.notifications.loading);
  $scope.contestTimeLeft = function(contest) {
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

  apiService.searchContests().success(function(contests) {
    //contests.sort(sortByStartDate);
    utils.sortByDateFn(contests, "startDateTime");
    $scope.contestsList = contests;
  }).finally(function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

});