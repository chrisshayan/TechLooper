angular.module('Chart').controller('chartController',
  function ($scope, jsonValue, connectionFactory, utils, headerService) {
    var events = jsonValue.events;
    //var rColor = 0;
    //var terms = [];
    var chartFactory = headerService.getChart().factory;

    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(events.terms, function (event, terms) {

      chartFactory.renderView(terms);

      //terms = data;
      //chartFactory.setTerms(terms);
      $.each(terms, function(index, item) {
        //chartFactory.updateViewTerm(term);
        //  rColor = rColor + 1;
        //  if (rColor == terms.length) {
        //    rColor = 0;
        //  }
        //  chartFactory.draw({
        //    'colorID': rColor,
        //    'count': term.count,
        //    'termID': term.term
        //  }, true);
        //
        $scope.$on(events.term + item.term, function (event, term) {
          chartFactory.updateViewTerm(term);
          //    rColor = rColor + 1;
          //    if (rColor == terms.length) {
          //      rColor = 0;
          //    }
          //    chartFactory.draw({
          //      'colorID': rColor,
          //      'count': data.count,
          //      'termName': data.termName,
          //      'termID': data.term,
          //      data: data
          //    });
        });
      });
      //chartFactory.initializeAnimation($scope);
    });

    connectionFactory.receiveTechnicalTerms();

  });