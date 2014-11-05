angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillCircleFactory) {
    connectionFactory.initialize($scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var top10 = utils.getTopItems(data.jobSkills, ["currentCount"], 10);
      $scope.term = data;
      $scope.top10 = top10;

      var top3 = utils.getTopItems(data.jobSkills, ["currentCount", "previousCount"], 3);
      console.log(top3);

      $scope.$apply();
      skillCircleFactory.draw(data, top10);
    });

    connectionFactory.analyticsSkill($routeParams.term);
    animationFactory.animatePage();
    //$scope.term = {count: 2000};

  });
