angular.module('Chart', [ "Bubble", "Pie", "Common", "Header" ]).controller(
   'chartController',
   [ "$scope", "jsonValue", "connectionFactory", "bubbleFactory", "pieFactory", "utils", "headerService", "$rootScope",
      function($scope, jsonValue, connectionFactory, bubbleFactory, pieFactory, utils, headerService, $rootScope) {
         $rootScope.$on(jsonValue.events.changeChart, function(event, data) {
            $rootScope.$doChart();
         });

         var events = jsonValue.events;
         var rColor = 0;
         var terms = [];
         var chartFactory;
         
         connectionFactory.initialize($scope);
         $rootScope.$doChart = function() {
            var chart = headerService.getChart();
            $('.chart-contrainer').empty().append((chart === jsonValue.charts.pie) ? jsonValue.ui.pie : jsonValue.ui.bubble);
            chartFactory = (chart === jsonValue.charts.pie) ? pieFactory : bubbleFactory;
            connectionFactory.receiveTechnicalTerms();
         }
         
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
         
         $rootScope.$doChart();
      } ]);