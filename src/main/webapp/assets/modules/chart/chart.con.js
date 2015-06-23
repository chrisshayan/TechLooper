angular.module('Chart').controller('chartController', function ($scope, jsonValue, connectionFactory,
                                                                utils, chartService, localStorageService, $route) {
  utils.sendNotification(jsonValue.notifications.switchScope, $scope);

  var events = jsonValue.events;
  var chartFactory = chartService.getChartFactory();

  $scope.$on(events.terms, function (event, terms) {
    chartFactory.renderView(terms);
    $.each(terms, function (index, item) {
      $scope.$on(events.term + item.term, function (event, term) {
        chartFactory.updateViewTerm(term);
      });
    });
    //pieFactory.switchChartData();
  });

  $scope.changeTo = function (type) {
    if (type == 'JOB') {
      localStorageService.set('PIE_CHART_ITEM_TYPE', jsonValue.pieChartType.job);
      //localStorageService.cookie.set('PIE_CHART_ITEM_TYPE', jsonValue.pieChartType.job);
      $route.reload();
    }
    else {
      //localStorageService.cookie.set('PIE_CHART_ITEM_TYPE', jsonValue.pieChartType.salary);
      localStorageService.set('PIE_CHART_ITEM_TYPE', jsonValue.pieChartType.salary);
      $route.reload();
    }
  }

  //$scope.type = localStorageService.cookie.get('PIE_CHART_ITEM_TYPE');
  $scope.type = localStorageService.get('PIE_CHART_ITEM_TYPE');
  $scope.type = $scope.type || "JOB";

  //$('.switch-data').find('li').removeClass('active');
  //$('.switch-data').find('li[data-chart=' + localStorageService.get('PIE_CHART_ITEM_TYPE') + ']').addClass('active');
  //console.log($('.switch-data').find('li[data-chart=' + localStorageService.get('PIE_CHART_ITEM_TYPE') + ']'));

  connectionFactory.receiveTechnicalTerms();
});