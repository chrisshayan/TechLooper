angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Home", []);
angular.module("Navigation", []);
angular.module("Footer", []);
angular.module("Chart", ["Common", "Bubble", "Pie", "Common"]);
angular.module("Jobs", ['infinite-scroll']);
angular.module("Pie", []);
angular.module("SearchForm", []);
angular.module("Skill", []);
angular.module("SignIn", []);
angular.module("Register", []);
angular.module("UserProfile", []);

var baseUrl = (function () {
  var paths = window.location.pathname.split('/');
  paths.pop();
  return window.location.protocol + '//' + window.location.host + paths.join('/');
})();

var techlooper = angular.module("Techlooper", [
  "pascalprecht.translate", "ngResource", "ngCookies", "ngRoute", "satellizer", "LocalStorageModule",
  "Bubble", "Pie", "Home", "Navigation", "Footer", "Common", "Chart", "Jobs", "Skill", "SignIn", "Register", "UserProfile"
]);

techlooper.config(["$routeProvider", "$translateProvider", "$authProvider", "localStorageServiceProvider", "$httpProvider",
  function ($routeProvider, $translateProvider, $authProvider, localStorageServiceProvider, $httpProvider) {
    $httpProvider.interceptors.push(function ($q, utils, jsonValue, localStorageService) {
        return {
          request: function (request) {
            return request || $q.when(request);
          },

          responseError: function (rejection) {
            switch (rejection.status) {
              case 403:
              case 401:
                if (localStorageService.get(jsonValue.storage.back2Me) === true) {
                  utils.sendNotification(jsonValue.notifications.loginFailed);
                }
                break;
              case 404:
                utils.sendNotification(jsonValue.notifications.http404);
                break;
            }
            return $q.reject(rejection);
          }
        };
      }
    );

    localStorageServiceProvider.setPrefix('techlooper')
      .setStorageType('cookieStorage')
      .setNotify(true, true);

    $.post("getSocialConfig", {providers: ["LINKEDIN", "FACEBOOK", "GOOGLE", "TWITTER", "GITHUB"]})
      .done(function (resp) {
        var oauth1Providers = ["TWITTER"];
        $.each(resp, function (i, prov) {
          $authProvider[prov.provider.toLowerCase()]({
            url: "auth/" + (oauth1Providers.indexOf(prov.provider) >= 0 ? "oath1/" : "") + prov.provider,
            clientId: prov.apiKey,
            redirectUri: prov.redirectUri
          });
        });
      });

    $translateProvider.useStaticFilesLoader({
      prefix: "modules/translation/messages_",
      suffix: ".json"
    });

    $translateProvider.registerAvailableLanguageKeys(['en', 'vi']);
    $translateProvider.fallbackLanguage("en");
    $translateProvider.preferredLanguage("en");
    $translateProvider.useLocalStorage();
    $translateProvider.use((window.navigator.userLanguage || window.navigator.language).substring(0, 2));

    $routeProvider
      .when("/bubble-chart", {
        templateUrl: "modules/bubble-chart/bubble-chart.tem.html",
        controller: "chartController"
      })
      .when("/pie-chart", {
        templateUrl: "modules/pie-chart/pie-chart.tem.html",
        controller: "chartController"
      })
      .when("/jobs/search", {
        templateUrl: "modules/job/searchForm.tem.html",
        controller: "searchFormController"
      })
      .when("/jobs/search/:text", {
        templateUrl: "modules/job/searchResult.tem.html",
        controller: "searchResultController"
      })
      .when("/analytics/skill/:term/:period?", {
        templateUrl: "modules/skill-analytics/skill-analytics.tem.html",
        controller: "skillAnalyticsController"
      })
      .when("/signin", {
        templateUrl: "modules/signin/signin.tem.html",
        controller: "signInController"
      })
      .when("/register", {
        templateUrl: "modules/register/register.tem.html",
        controller: "registerController"
      })
      .when("/user", {
        templateUrl: "modules/user-profile/user-profile.tem.html",
        controller: "userProfileController"
      })
      .otherwise({
        redirectTo: "/bubble-chart"
      });
  }]);

techlooper.run(function (shortcutFactory, connectionFactory, loadingBoxFactory, cleanupFactory,
                         tourService, signInService, historyFactory, userService, routerService, $location, utils, $rootScope) {
  shortcutFactory.initialize();
  connectionFactory.initialize();
  loadingBoxFactory.initialize();
  cleanupFactory.initialize();
  //tourService.initialize();
  historyFactory.initialize();
  routerService.initialize();
  userService.initialize();
  signInService.init();

  var locationPathFn = $location.path;
  $location.path = function () {
    var rsLocationPathFn = locationPathFn.apply($location, arguments);
    utils.apply();
    return rsLocationPathFn;
  }
});

techlooper
  .directive("navigation", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/navigation/navigation.tem.html",
      controller: "navigationController"
    }
  })
  .directive("findjobs", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/job/findJobs.tem.html"
    }
  });