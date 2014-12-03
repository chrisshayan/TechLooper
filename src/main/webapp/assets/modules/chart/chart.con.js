angular.module('Chart').controller('chartController',
  function ($scope, jsonValue, connectionFactory, utils, headerService, bootstrapTourFactory) {
    var events = jsonValue.events;
    var chartFactory = headerService.getChart().factory;
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
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