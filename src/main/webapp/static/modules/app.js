angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Chart", [ "Bubble" ]);

var techlooper = angular.module("Techlooper", [ "Common", "Chart", "ui.router" ]);

techlooper.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
   $urlRouterProvider.otherwise('/');
   $stateProvider.state('home', {
      url : '/',
      views : {
         "" : {
            templateUrl : "modules/home/home.template.html"
         },
         "find-jobs@home" : {
            templateUrl : "modules/find-jobs/findjobs.template.html"
         },
         "chart@home" : {
            templateUrl : "modules/collection/chart.template.html",
            controller : "chartController"
         }
      }
   }).state('home.bubble', {
      url : 'bubble',
      templateUrl: "modules/bubble/bubble.template.html"
   });
   $locationProvider.html5Mode(true);
});

// techlooper.controller("defaultController", ["$scope", "connectionService",
// function($scope, connectionService) {
// $scope.technicalTerms = connectionService.fetchTerms();
// console.log($scope.technicalTerms);
// }]);
