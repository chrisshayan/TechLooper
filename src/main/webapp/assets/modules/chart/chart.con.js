angular.module('Chart').controller('chartController', function ($scope, jsonValue, connectionFactory,
                                                                utils, chartService, termService) {
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
  });
  connectionFactory.receiveTechnicalTerms();
});