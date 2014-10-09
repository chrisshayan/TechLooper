angular.module('Chart', [ "Bubble", "Pie", "Common", "Header" ]).controller(
   'chartController',
   [ "$scope", "jsonValue", "connectionFactory", "bubbleFactory", "pieFactory", "utils", "headerService", "$rootScope",
      function($scope, jsonValue, connectionFactory, bubbleFactory, pieFactory, utils, headerService, $rootScope) {
         $rootScope.$on(jsonValue.events.changeChart, function(event, data) {
            $rootScope.$doChart(data);
         });

         var events = jsonValue.events;
         var rColor = 0;
         var terms = [];
         var chartFactory;
         
         connectionFactory.initialize($scope);
         $rootScope.$doChart = function(chart) {
            if (chart === undefined) {
               chart = headerService.getChart();
            }
            
            var chartDiv = "";
            switch (chart) {
            case jsonValue.charts.pie :
               chartDiv = jsonValue.ui.pie;
               chartFactory = pieFactory;
               break;
            case jsonValue.charts.bubble :
               chartDiv = jsonValue.ui.bubble;
               chartFactory = bubbleFactory;
               break;
            }
            $('.chart-contrainer').empty().append(chartDiv);
            connectionFactory.receiveTechnicalTerms();
         }
         
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
         
         $rootScope.$doChart();
      } ]);