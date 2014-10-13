angular.module('Chart', [ "Bubble", "Pie", "Common", "Header" ]).controller('chartController',
   [ "$scope", "jsonValue", "connectionFactory", "utils", "headerService", "$rootScope", function($scope, jsonValue, connectionFactory, utils, headerService, $rootScope) {
      var events = jsonValue.events;
      var rColor = 0;
      var terms = [];
      var chartFactory = headerService.getChart().factory;

      connectionFactory.initialize($scope);
      $scope.$on(events.terms, function(event, data) {
         terms = data;
         chartFactory.setTerms(terms);
         $.each(terms, function(index, term) {
            rColor = rColor + 1;
            if (rColor == terms.length) {
               rColor = 0;
            }
            chartFactory.draw({
               'colorID' : rColor,
               'count' : term.count,
               'termName' : term.name,
               'termID' : term.term
            }, true);

            $scope.$on(events.term + term.term, function(event, data) {
               rColor = rColor + 1;
               if (rColor == terms.length) {
                  rColor = 0;
               }
               chartFactory.draw({
                  'colorID' : rColor,
                  'count' : data.count,
                  'termName' : data.termName,
                  'termID' : data.term
               });
            });

         });

         chartFactory.initializeAnimation();
      });

      connectionFactory.receiveTechnicalTerms();
   } ]);