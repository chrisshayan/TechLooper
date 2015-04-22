techlooper.controller("technicalDetailController", function (utils, connectionFactory, $routeParams,
                                                             technicalDetailService, $scope, $timeout, jsonValue, termService) {

  $scope.jobLevels = jsonValue.jobLevels;

  $scope.termRequest = {term: $routeParams.term};
  $scope.selectJobLevel = function (jobLevel) {
    $scope.termRequest.jobLevelId = jobLevel.id;
    if ($scope.termRequest.jobLevelId === -1) {
      delete $scope.termRequest.jobLevelId;
    }
    $scope.loadData();
  }

  var termView = termService.toViewTerm($scope.termRequest);

  $scope.showCircle = function (skill) {
    var shownMe = $("#circles-" + skill.id + " > .circles-wrp").length > 0;
    if (shownMe) return true;
    technicalDetailService.showSkillsList(skill, $scope.termStatistic.totalJob);
    return true;
  }

  $scope.companyUrl = function (company) {//java-fpt+at-it-software-i35-en
    return sprintf("http://vietnamworks.com/%s-%s+at-it-software-i35-en", termView.label, company.name);
  }

  $scope.loadData = function () {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    connectionFactory.termStatisticInOneYear($scope.termRequest)
      .success(function (data, status, headers, config) {
        $scope.termRequest.skills = data.skills.map(function (skill) {return skill.skillName;});
        $.each(data.skills, function (i, skill) {skill.id = i;});
        $scope.termStatistic = termService.toViewTerm(data);
        technicalDetailService.trendSkills($scope.termStatistic);
        utils.sendNotification(jsonValue.notifications.loaded);
      })
      .error(function (data, status, headers, config) {
      });
  }

  $scope.loadData();
});
