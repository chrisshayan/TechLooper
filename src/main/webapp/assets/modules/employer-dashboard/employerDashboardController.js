techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService, $location) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  apiService.getEmployerDashboardInfo()
    .success(function (data) {
      data.challenges.sort(function (left, right) {
        var rightStartDate = moment(right.startDateTime, jsonValue.dateFormat);
        var leftStartDate = moment(left.startDateTime, jsonValue.dateFormat);
        return rightStartDate.isAfter(leftStartDate);
      });
      data.projects.sort(function (left, right) {
        return right.projectId - left.projectId;
      });
      $scope.dashboardInfo = data;
    })
    .finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});

  $scope.myFunction = function (val) {
    return val.progress.translate != 'notStart' && val.progress.translate != 'closed';
  };

  $scope.toEditPage = function(challenge) {
    $location.url("post-challenge?id=" + challenge.challengeId);
  }

  $scope.deleteCurrentChallenge = function (challenge) {
    $("#challenge-" + challenge.challengeId).find("td")
      .animate({padding: 0}).wrapInner("<div />").children("div")
      .slideUp(100, function () {
        var index = $.inArray(challenge, $scope.dashboardInfo.challenges);
        if (index < 0) {
          return;
        }
        $scope.dashboardInfo.challenges.splice(index, 1);
        $scope.$apply();
      });

    //apiService.deleteChallengeById(challenge.challengeId)
    //  .success(function () {
    //    $("#challenge-" + challenge.challengeId).find("td")
    //      .animate({padding: 0}).wrapInner("<div />").children("div")
    //      .slideUp(100, function () {
    //        var index = $.inArray(challenge, $scope.dashboardInfo.challenges);
    //        if (index < 0) {
    //          return;
    //        }
    //        $scope.dashboardInfo.challenges.splice(index, 1);
    //        $scope.$apply();
    //      });
    //  });
  };
});
