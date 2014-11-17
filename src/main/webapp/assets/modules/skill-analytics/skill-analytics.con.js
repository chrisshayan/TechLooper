angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService, $location) {
    var period = $location.search().period;
    var skillStatisticRequest = {
      term: $routeParams.term,
      histograms: skillAnalyticsService.getHistograms(period),
      period: period
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
