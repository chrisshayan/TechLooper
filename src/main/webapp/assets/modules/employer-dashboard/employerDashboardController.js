techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService, $location, $filter) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  var sortByStartDate = function (left, right) {
    var rightStartDate = moment(right.startDateTime, jsonValue.dateFormat);
    var leftStartDate = moment(left.startDateTime, jsonValue.dateFormat);
    var before = rightStartDate.isBefore(leftStartDate, "day");
    var same = rightStartDate.isSame(leftStartDate, "day");
    return same ? 0 : (before ? -1 : 1);
  };

  apiService.getEmployerDashboardInfo()
    .success(function (data) {
      data.challenges.sort(sortByStartDate);

      data.activeChallenges = $filter("progress")(data.challenges, "challenges", [jsonValue.status.registration, jsonValue.status.progress]);
      data.notStartedChallenges = $filter("progress")(data.challenges, "challenges", jsonValue.status.notStarted);
      data.closedChallenges = $filter("progress")(data.challenges, "challenges", jsonValue.status.closed);

      data.projects.sort(function (left, right) {return right.projectId - left.projectId;});

      $scope.dashboardInfo = data;
      $scope.changeChallengesByStatus();
    })
    .finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});

  $scope.myFunction = function (val) {
    return val.progress.translate != 'notStart' && val.progress.translate != 'closed';
  };

  $scope.toEditPage = function (challenge) {
    $location.url("post-challenge?a=edit&id=" + challenge.challengeId);
  }

  $scope.changeChallengesByStatus = function (status) {
    switch (status) {
      case "active":
        $scope.challengesByStatus = {
          challenges: $scope.dashboardInfo.activeChallenges,
          status: "active"
        }
        break;

      case "notStarted":
        $scope.challengesByStatus = {
          challenges: $scope.dashboardInfo.notStartedChallenges,
          status: "notStarted"
        }
        break;

      case "closed":
        $scope.challengesByStatus = {
          challenges: $scope.dashboardInfo.closedChallenges,
          status: "closed"
        }
        break;

      default:
        $scope.challengesByStatus = {
          challenges: $scope.dashboardInfo.challenges
        }
    }
  }

  $scope.filterChallenges = function (status) {
    if (!$scope.dashboardInfo) return [];
    var challenges = $scope.dashboardInfo.challenges || [];
    return $filter("progress")(challenges, "challenges", status || $scope.challengeStatus);
  }

  $scope.deleteCurrentChallenge = function (challenge) {
    var deleteById = function () {
      $("#challenge-" + challenge.challengeId).find("td")
        .animate({padding: 0}).wrapInner("<div />").children("div")
        .slideUp(100, function () {
          var index = $.inArray(challenge, $scope.dashboardInfo.challenges);
          if (index < 0) {
            return;
          }
          $scope.dashboardInfo.challenges.splice(index, 1);
          if (!$scope.filterChallenges().length) $scope.changeChallengeStatus();
          $scope.$apply();
        });
    }
    //deleteById();
    apiService.deleteChallengeById(challenge.challengeId)
      .success(function () {
        deleteById();
      });
  };
});
