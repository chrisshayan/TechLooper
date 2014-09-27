angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Home", []);
angular.module("Header", []);
angular.module("Footer", []);
angular.module("Chart", []);

var techlooper = angular.module("Techlooper",
      [ "ngResource", "ngRoute", "Home", "Header", "Footer", "Common", "Chart" ]);

techlooper.config(function($routeProvider) {
   // $urlRouterProvider.otherwise('/');
   $routeProvider.when('/', {
      templateUrl : "modules/home/home.tpl.html",
      controller : "homeController"
   }).otherwise({
      redirectTo : '/'
   });
   // $locationProvider.html5Mode(true);
});

techlooper.directive('header', function() {
   return {
      restrict : 'A', // This mens that it will be used as an attribute and NOT as an element.
      replace : true,
      templateUrl : "modules/header/header.tpl.html",
      controller : "headerController"
   }
}).directive('footer', function() {
   return {
      restrict : 'A', // This mens that it will be used as an attribute and NOT as an element.
      replace : true,
      templateUrl : "modules/footer/footer.tpl.html",
      controller : "footerController"
   }
});
