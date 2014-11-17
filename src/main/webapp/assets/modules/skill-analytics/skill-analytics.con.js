angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService, $location) {
    var skillStatisticRequest = {
      term: $routeParams.term,
      period: $routeParams.period &&
          ($routeParams.period === "week" || $routeParams.period === "month" || $routeParams.period === "quarter") ?
           $routeParams.period : "month",
      histograms: skillAnalyticsService.getHistograms(this.period)
    };

    $('.loading-data').show();
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var viewJson = skillAnalyticsService.extractViewJson(data, skillStatisticRequest);
      $scope.viewJson = viewJson;
      skillTableFactory.calculatePercentage(viewJson);
      $scope.$apply();

      skillCircleFactory.renderView(viewJson);
      skillTableFactory.renderView(viewJson);
      skillChartFactory.renderView(viewJson);

      $('.loading-data').hide();
      skillAnalyticsService.registerEvents();
    });

    connectionFactory.analyticsSkill(skillStatisticRequest);
    animationFactory.animatePage();
  });
