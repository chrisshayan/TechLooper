angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils) {
    connectionFactory.initialize($scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var top10 = utils.getTopItems(data.jobSkills, "currentCount", 10);
      console.log(top10);

      var top3 = utils.getTopItems(data.jobSkills, ["currentCount", "previousCount"], 3);
    });

    connectionFactory.analyticsSkill($routeParams.term);

    animationFactory.animatePage();
});
