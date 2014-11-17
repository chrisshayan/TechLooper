angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService, $location) {
    var period = $location.search();
    //console.log(param.period);
    //$location.search("period", "month");
    //$scope.$apply();
    var skillStatisticRequest = {term: $routeParams.term, histograms: skillAnalyticsService.getHistograms(period)};

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
