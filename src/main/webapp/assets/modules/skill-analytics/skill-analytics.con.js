angular.module('Skill').controller('skillAnalyticsController',
  function ($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
            skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService) {
    $('.loading-data').show();
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);

    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var viewJson = skillAnalyticsService.map(data);
      var circleItems = skillAnalyticsService.getCirclesJson(viewJson);
      var tableAndChartItems = skillAnalyticsService.getTableAndChartJson(viewJson, circleItems);

      $scope.term = viewJson;
      $scope.circleItems = circleItems;
      $scope.tableAndChartItems = skillTableFactory.reformatData(tableAndChartItems);
      $scope.$apply();

      // render left circle chart
      skillCircleFactory.renderView(viewJson, circleItems);

      // render bottom-right table & top-right line-chart
      skillTableFactory.formatDate();
      skillChartFactory.renderView(tableAndChartItems);

      $('.loading-data').hide();
      skillAnalyticsService.registerEvents();
    });

    connectionFactory.analyticsSkill($routeParams.term);
    animationFactory.animatePage(); //TODO

    $('.btn-close').click(function () {
      shortcutFactory.trigger('esc');
    });
    $('.btn-logo').click(function () {
      shortcutFactory.trigger('esc');
    });
  });
