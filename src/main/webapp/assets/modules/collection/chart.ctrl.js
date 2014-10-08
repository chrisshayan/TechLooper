angular.module('Chart', ["Bubble", "Pie" , "Common", "Header"]).controller('chartController', 
   ["$scope", "jsonValue", "connectionFactory", "bubbleFactory", "pieFactory", "utils", "headerService", 
   function ($scope, jsonValue, connectionFactory, bubbleFactory, pieFactory, utils, headerService) {

   connectionFactory.initialize($scope);
   var events = jsonValue.events;
   var rColor = 0;
   var terms = [];
   var chartFactory = headerService.getChart() === jsonValue.charts.pie ? pieFactory : bubbleFactory;
   $scope.$on(events.terms, function (event, data) {
      terms = data;
      chartFactory.setTerms(terms);
      $.each(terms, function (index, term) {
         rColor = rColor + 1;
         if (rColor == 9) {
            rColor = 0;
         }
         chartFactory.draw({
            'colorID': rColor,
            'count': term.count,
            'termName': term.name,
            'termID': term.term
         }, true);

         $scope.$on(events.term + term.term, function (event, data) {
            rColor = rColor + 1;
            if (rColor == 9) {
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
      chartFactory.initializeAnimation();
   });
   connectionFactory.receiveTechnicalTerms();
}]);