techlooper.controller("technicalDetailController", function (utils, connectionFactory, $routeParams,
                                                             technicalDetailService, $scope, $timeout, jsonValue) {
  var term = $routeParams.term;

  // TODO: write a blog about dom manipulation with angularjs
  $scope.showCircle = function(skill) {
    technicalDetailService.showSkillsList(skill);
    return true;
  }

  $scope.companyUrl = function(company) {//java-fpt+at-it-software-i35-en
    return sprintf("http://vietnamworks.com/%s-%s+at-it-software-i35-en", term.toLowerCase(), company.name);
  }

  connectionFactory.termStatisticInOneYear({term: term})
    .success(function (data, status, headers, config) {
      $scope.termStatistic = data;
      technicalDetailService.trendSkills($scope.termStatistic);
    })
    .error(function (data, status, headers, config) {
    });
});
