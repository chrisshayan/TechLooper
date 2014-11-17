angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService, $location) {
    var period = $location.search();
    //console.log(param.period);
    //$location.search("period", "month");
    //$scope.$apply();

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

    connectionFactory.analyticsSkill({term: $routeParams.term, histograms: skillAnalyticsService.getHistograms(period)});
    animationFactory.animatePage();
  });
