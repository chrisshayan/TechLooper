angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Home", []);
angular.module("Header", []);
angular.module("Footer", []);
angular.module("Chart", ["Common", "Bubble", "Pie", "Common", "Header"]);
angular.module("Jobs", ['infinite-scroll']);
angular.module("Pie", []);
angular.module("SearchForm", []);

var techlooper = angular.module("Techlooper", [
  "pascalprecht.translate", "ngResource", "ngCookies", "ngRoute",
  "Bubble", "Pie", "Home", "Header", "Footer", "Common", "Chart", "Jobs"
]);

techlooper.config(["$routeProvider", "$translateProvider", "$locationProvider",
  function ($routeProvider, $translateProvider, $locationProvider, jsonVal) {
    //uiSelectConfig.theme = 'select2';

    $translateProvider.useStaticFilesLoader({
      prefix: "modules/translation/messages_",
      suffix: ".json"
    });

    $translateProvider.registerAvailableLanguageKeys(['en', 'vi']);
    $translateProvider.fallbackLanguage("en");
    $translateProvider.preferredLanguage("en");
    $translateProvider.useLocalStorage();
    $translateProvider.use((window.navigator.userLanguage || window.navigator.language).substring(0, 2));

    $routeProvider.when("/bubble-chart", {
      templateUrl: "modules/bubble-chart/bubble-chart.tem.html",
      controller: "chartController"
    }).when("/pie-chart", {
      templateUrl: "modules/pie-chart/pie-chart.tem.html",
      controller: "chartController"
    }).when("/jobs/search", {
      templateUrl: "modules/job/searchForm.tem.html",
      controller: "searchFormController"
    }).when("/jobs/search/:text", {
      templateUrl: "modules/job/searchResult.tem.html",
      controller: "searchResultController"
    }).otherwise({
      redirectTo: "/bubble-chart"
    });
  }]);

techlooper.directive("header", function () {
  return {
    restrict: "A", // This means that it will be used as an attribute and NOT as an element.
    replace: true,
    templateUrl: "modules/header/header.tem.html",
    controller: "headerController"
  }
}).directive("findjobs", function () {
  return {
    restrict: "A", // This means that it will be used as an attribute and NOT as an element.
    replace: true,
    templateUrl: "modules/job/findJobs.tem.html"
  }
});