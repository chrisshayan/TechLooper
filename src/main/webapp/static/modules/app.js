angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Chart", ["Bubble"]);

var techlooper = angular.module("Techlooper", [ "Common", "Chart", "ui.router" ]);

techlooper.config(function($stateProvider, $urlRouterProvider) {
   $urlRouterProvider.otherwise('/home');
   $stateProvider.state('home', {
      url : '/home',
      views : {
         "" : {
            templateUrl : "home/home.template.html"
         },
         "analystic-find-jobs" : {
            templateUrl : "analystic-find-jobs/ana.template.html"
         },
         "chart" : {
            templateUrl : "chart/chart.template.html",
            controller : "chartController"
         }
      }
   });
});

// techlooper.controller("defaultController", ["$scope", "connectionService",
// function($scope, connectionService) {
// $scope.technicalTerms = connectionService.fetchTerms();
// console.log($scope.technicalTerms);
// }]);
