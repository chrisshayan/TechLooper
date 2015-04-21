techlooper.controller("technicalDetailController", function (utils, connectionFactory, $routeParams,
                                                             technicalDetailService, $scope, $timeout, jsonValue, termService) {
  utils.sendNotification(jsonValue.notifications.switchScope, $scope);
  var term = $routeParams.term;
  var termView = termService.toViewTerm({term: term});

  $scope.showCircle = function(skill) {
    technicalDetailService.showSkillsList(skill, $scope.termStatistic.totalJob);
    return true;
  }

  $scope.companyUrl = function(company) {//java-fpt+at-it-software-i35-en
    return sprintf("http://vietnamworks.com/%s-%s+at-it-software-i35-en", termView.label, company.name);
  }

  connectionFactory.termStatisticInOneYear({term: term})
    .success(function (data, status, headers, config) {
      $scope.termStatistic = termService.toViewTerm(data);
      technicalDetailService.trendSkills($scope.termStatistic);
      utils.sendNotification(jsonValue.notifications.loaded);
    })
    .error(function (data, status, headers, config) {
    });
});
