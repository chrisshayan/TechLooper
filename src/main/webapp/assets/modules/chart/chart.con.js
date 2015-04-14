angular.module('Chart').controller('chartController', function ($scope, jsonValue, connectionFactory, utils, chartService, navigationService) {
  utils.sendNotification(jsonValue.notifications.switchScope, $scope);

  var events = jsonValue.events;
  var chartFactory = chartService.getChartFactory();

  $scope.$on(events.terms, function (event, terms) {
    $.each(terms, function(i, term) {
      if ($.isNumeric(term.averageSalaryMin) &&  $.isNumeric(term.averageSalaryMax)) {
        term.salRange = sprintf("%(averageSalaryMin)s - %(averageSalaryMax)s", term);
      }
      else if ($.isNumeric(term.averageSalaryMin)) {
        term.salRange = sprintf("from %(averageSalaryMin)s", term);
      }
      else {
        term.salRange = sprintf("upto %(averageSalaryMax)s", term);
      }
    });
    chartFactory.renderView(terms);
    $.each(terms, function (index, item) {
      $scope.$on(events.term + item.term, function (event, term) {
        chartFactory.updateViewTerm(term);
      });
    });
  });
  connectionFactory.receiveTechnicalTerms();
});