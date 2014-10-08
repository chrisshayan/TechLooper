angular.module('Chart', [ "Bubble", "Pie", "Common", "Header" ]).controller(
   'chartController',
   [ "$scope", "jsonValue", "connectionFactory", "bubbleFactory", "pieFactory", "utils", "headerService", "$rootScope",
      function($scope, jsonValue, connectionFactory, bubbleFactory, pieFactory, utils, headerService, $rootScope) {
         $rootScope.$on(jsonValue.events.changeChart, function(event, data) {
            $rootScope.$doChart();
         });

         var doChart = function() {
            var events = jsonValue.events;
            var rColor = 0;
            var terms = [];
            var chart = headerService.getChart();
            var chartFactory = (chart === jsonValue.charts.pie) ? pieFactory : bubbleFactory;
            $('.chart-contrainer').empty().append((chart === jsonValue.charts.pie) ? jsonValue.ui.pie : jsonValue.ui.bubble);

            connectionFactory.initialize($scope);

            $scope.$on(events.terms, function(event, data) {
               terms = data;
               chartFactory.setTerms(terms);
               $.each(terms, function(index, term) {
                  rColor = rColor + 1;
                  if (rColor == 9) {
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
                     if (rColor == 9) {
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
         }
         $rootScope.$doChart = doChart;
         
         
         console.log("chart controller doChart");
         doChart();
      } ]);