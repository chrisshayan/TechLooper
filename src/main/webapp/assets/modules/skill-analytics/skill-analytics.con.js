angular.module('Skill').controller('skillAnalyticsController',
  function($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
    skillCircleFactory, skillChartFactory, skillAnalyticsService) {
    $('.loading-data').show();
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var viewJson = skillAnalyticsService.extractViewJson(data);
      $scope.viewJson = viewJson;
      skillTableFactory.calculatePercentage(viewJson);
      $scope.$apply();

      skillCircleFactory.renderView(viewJson);
      skillTableFactory.renderView(viewJson);
      skillChartFactory.renderView(viewJson);

        $('.loading-data').hide();
        skillAnalyticsService.registerEvents();
        skillAnalyticsService.chartManagement();
    });

    connectionFactory.analyticsSkill($routeParams.term);
    animationFactory.animatePage(); //TODO
});
