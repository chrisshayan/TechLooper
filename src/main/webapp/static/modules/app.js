angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Home", []);
angular.module("Header", [ "Common" ]);
angular.module("Chart", [ "Bubble" ]);

var techlooper = angular.module("Techlooper", [ "Home", "Header", "Common", "Chart", "ui.router" ]);

techlooper.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
   $urlRouterProvider.otherwise('/');
   $stateProvider.state('home', {
      url : '/',
      views : {
         "" : {
            templateUrl : "modules/home/home.tpl.html",
            controller : "homeController"
         },
         "find-jobs@home" : {
            templateUrl : "modules/find-jobs/find-jobs.tpl.html"
         },
         "chart@home" : {
            templateUrl : "modules/collection/chart.tpl.html",
            controller : "chartController"
         },
         "header@home" : {
            templateUrl : "modules/header/header.tpl.html",
            controller : "headerController"
         }
      }
   });
   // $locationProvider.html5Mode(true);
});

techlooper.directive('header', function () {
   return {
       restrict: 'A', //This menas that it will be used as an attribute and NOT as an element.
       replace: true,
       templateUrl: "modules/header/header.tpl.html",
       controller: "headerController"
   }
});
