techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService, $location, $filter, $route) {
  //$('.summernote').summernote();
  //var edit = function () {
  //$('.summernote').summernote({
  //  toolbar: [
  //    ['fontname', ['fontname']],
  //    ['fontsize', ['fontsize']],
  //    ['style', ['bold', 'italic', 'underline', 'clear']],
  //    ['color', ['color']],
  //    ['para', ['ul', 'ol', 'paragraph']],
  //    ['height', ['height']],
  //    ['table', ['table']],
  //    ['insert', ['link']],
  //    ['misc', ['undo', 'redo', 'codeview', 'fullscreen']]
  //  ]
  //});
  //var save = function () {
  //  var aHTML = $('.click2edit').code(); //save HTML If you need(aHTML: array).
  //  $('.click2edit').destroy();
  //};

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.composeEmail = {
    send: function() {
      console.log($scope.composeEmail);
      var content = $('.click2edit').code();
      console.log(content);
    },
    cancel: function() {
      $location.search({});
    }
  };

  var param = $location.search();
  if (!$.isEmptyObject(param)) {
    if (param.a == "challenge-daily-mail-registrants") {
      var challengeId = param.challengeId;
      var now = param.currentDateTime;
      apiService.getDailyChallengeRegistrantNames(challengeId, now)
        .success(function (names) {
          $scope.composeEmail.names = names.join("; ");
          $("#emailCompose").modal();
        });
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
