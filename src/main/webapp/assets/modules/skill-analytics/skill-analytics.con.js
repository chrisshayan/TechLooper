angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService, $location, navigationService) {
    var histogramsAndPeriod = skillAnalyticsService.getHistogramsAndPeriod($routeParams.period);
    var skillStatisticRequest = {
      term: $routeParams.term,
      period: histogramsAndPeriod.period,
      histograms: histogramsAndPeriod.histograms
    };
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var viewJson = skillAnalyticsService.extractViewJson(data, skillStatisticRequest);
      $scope.viewJson = viewJson;
      skillTableFactory.calculatePercentage(viewJson);
      utils.apply();

      skillCircleFactory.renderView(viewJson);
      skillTableFactory.renderView(viewJson);
      skillChartFactory.renderView(viewJson);
      skillAnalyticsService.registerEvents();
    });

    connectionFactory.analyticsSkill(skillStatisticRequest);
    //animationFactory.animatePage();
  });
