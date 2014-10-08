angular.module('Chart', ["Bubble", "Common"]).controller('chartController', ["$scope", "jsonValue", "connectionFactory", "bubbleFactory", "utils", function ($scope, jsonValue, connectionFactory, bubbleFactory, utils) {
   connectionFactory.initialize($scope);
   var events = jsonValue.events;
   var rColor = 0;
   var terms = [];
   $scope.$on(events.terms, function (event, data) {
      terms = data;
      bubbleFactory.setTerms(terms);
      $.each(terms, function (index, term) {
         rColor = rColor + 1;
         if (rColor == 9) {
            rColor = 0;
         }
         bubbleFactory.draw({
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
            bubbleFactory.draw({
               'colorID': rColor,
               'count': data.count,
               'termName': data.termName,
               'termID': data.term
            });
         });

      });
      bubbleFactory.initializeAnimation();
   });
   connectionFactory.receiveTechnicalTerms();
}]);