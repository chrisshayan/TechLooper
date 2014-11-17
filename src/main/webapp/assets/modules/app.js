angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Home", []);
angular.module("Header", []);
angular.module("Footer", []);
angular.module("Chart", ["Common", "Bubble", "Pie", "Common", "Header"]);
angular.module("Jobs", ['infinite-scroll']);
angular.module("Pie", []);
angular.module("SearchForm", []);
angular.module("Skill", []);


var techlooper = angular.module("Techlooper", [
  "pascalprecht.translate", "ngResource", "ngCookies", "ngRoute",
  "Bubble", "Pie", "Home", "Header", "Footer", "Common", "Chart", "Jobs", "Skill"
]);

techlooper.config(["$routeProvider", "$translateProvider", "$locationProvider",
  function ($routeProvider, $translateProvider, $locationProvider, jsonVal) {

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
    }).when("/analytics/skill/:term/:period?", {
      templateUrl: "modules/skill-analytics/skill-analytics.tem.html",
      controller: "skillAnalyticsController"
    }).otherwise({
      redirectTo: "/bubble-chart"
    });
  }]);

techlooper.run(function(shortcutFactory, connectionFactory, loadingBoxFactory) {
  shortcutFactory.initialize();
  connectionFactory.initialize();
  loadingBoxFactory.initialize();
});

techlooper.directive("header", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/header/header.tem.html",
    controller: "headerController"
  }
}).directive("findjobs", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/job/findJobs.tem.html"
  }
});