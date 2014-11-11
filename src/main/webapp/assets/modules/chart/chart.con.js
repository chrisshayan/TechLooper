angular.module('Chart').controller('chartController',
  function ($scope, jsonValue, connectionFactory, utils, headerService) {
    var events = jsonValue.events;
    var rColor = 0;
    var terms = [];
    var chartFactory = headerService.getChart().factory;

    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(events.terms, function (event, data) {
      terms = data;
      chartFactory.setTerms(terms);
      $.each(terms, function (index, term) {
        rColor = rColor + 1;
        if (rColor == terms.length) {
          rColor = 0;
        }
        chartFactory.draw({
          'colorID': rColor,
          'count': term.count,
          'termID': term.term
        }, true);

        $scope.$on(events.term + term.term, function (event, data) {
          rColor = rColor + 1;
          if (rColor == terms.length) {
            rColor = 0;
          }
          chartFactory.draw({
            'colorID': rColor,
            'count': data.count,
            'termName': data.termName,
            'termID': data.term
          });
        });
      });

      chartFactory.initializeAnimation($scope);

    });

    connectionFactory.receiveTechnicalTerms();
     
  });