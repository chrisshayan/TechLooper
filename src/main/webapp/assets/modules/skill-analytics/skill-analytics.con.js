angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService, $location) {
    var skillStatisticRequest = {
      term: $routeParams.term,
      period: $routeParams.period,
      histograms: skillAnalyticsService.getHistograms($routeParams.period)
    };
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var viewJson = skillAnalyticsService.extractViewJson(data, skillStatisticRequest);
      $scope.viewJson = viewJson;
      skillTableFactory.calculatePercentage(viewJson);
      $scope.$apply();

      skillCircleFactory.renderView(viewJson);
      skillTableFactory.renderView(viewJson);
      skillChartFactory.renderView(viewJson);
      skillAnalyticsService.registerEvents();
    });

    connectionFactory.analyticsSkill(skillStatisticRequest);
    animationFactory.animatePage();
  });
