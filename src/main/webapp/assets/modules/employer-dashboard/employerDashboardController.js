techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService, $location, $filter, $route) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.composeEmail = {
    send: function() {
      $scope.composeEmail.content = $('.summernote').code();
      if($scope.composeEmail.content == '<p><br></p>'){
        return;
      }
      apiService.sendEmailToDailyChallengeRegistrants($scope.composeEmail.challengeId, $scope.composeEmail.now, $scope.composeEmail)
        .finally(function() {
          $scope.composeEmail.cancel();
        });
    },
    cancel: function() {
      $location.search({});
      $('.modal-backdrop').remove();
    }
  };

  var param = $location.search();
  if (!$.isEmptyObject(param)) {
    if (param.a == "challenge-daily-mail-registrants") {
      var challengeId = param.challengeId;
      var now = param.currentDateTime;
      $scope.composeEmail.challengeId = challengeId;
      $scope.composeEmail.now = now;
      apiService.getDailyChallengeRegistrantNames(challengeId, now)
        .success(function (names) {
          $scope.composeEmail.names = names.join("; ");
          $("#emailCompose").modal();
        });
    }
    else if (param.a == "feedback-registrant") {
      //TODO feedback email
    }
  }

  apiService.getEmployerDashboardInfo()
    .success(function (data) {
      utils.sortByDate(data.challenges, "startDateTime");
      data.projects.sort(function (left, right) {return right.projectId - left.projectId;});
      $scope.dashboardInfo = data;
    })
    .finally(function () {
      utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
    });

  $scope.changeChallengeStatus = function (status) {
    $scope.challengeStatus = status;
  };

  $scope.toEditPage = function (challenge) {
    $location.url("post-challenge?a=edit&id=" + challenge.challengeId);
  };

  $scope.filterChallenges = function (status) {
    if (!$scope.dashboardInfo) return [];
    var challenges = $scope.dashboardInfo.challenges || [];
    return $filter("progress")(challenges, "challenges", status || $scope.challengeStatus);
  };

  $scope.deleteCurrentChallenge = function (challenge) {
    var deleteById = function () {
      $("#challenge-" + challenge.challengeId).find("td")
        .animate({padding: 0}).wrapInner("<div />").children("div")
        .slideUp(100, function () {
          //console.log(challenge);
          var index = $.inArray(challenge, $scope.dashboardInfo.challenges);
          if (index < 0) {
            return;
          }
          $scope.dashboardInfo.challenges.splice(index, 1);
          var challenges = $filter("progress")($scope.dashboardInfo.challenges, "challenges", jsonValue.status.notStarted);
          if (!challenges.length) $scope.changeChallengeStatus();
          $scope.$apply();
        });
    };
    //deleteById();
    apiService.deleteChallengeById(challenge.challengeId)
      .success(function () {
        deleteById();
      });
  };

  $scope.goToChallengeDetails = function (challenge) {
    $location.url("challenge-detail/-" + challenge.challengeId + "-id?a=registrants");
  }
});
