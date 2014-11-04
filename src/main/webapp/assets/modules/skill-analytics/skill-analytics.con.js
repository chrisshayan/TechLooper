angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory) {
    //connectionFactory.initialize($scope);
    //$scope.$on(jsonValue.events.analyticsSkill, function (event, skills) {
    //  // top 10 currents
    //  jsonPath.eval(skills, "$.jobSkills.currentCount");
    //});
    //
    //connectionFactory.analyticsSkill($routeParams.term);

    animationFactory.animatePage();

    //skillFactory.drawCircle($scope);
  });

