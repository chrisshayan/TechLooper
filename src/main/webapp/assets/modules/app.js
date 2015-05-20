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
  "Bubble", "Pie", "Home", "Navigation", "Footer", "Common", "Chart", "Jobs", "Skill", "SignIn", "Register", "UserProfile", "selectize"
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
                if (localStorageService.get(jsonValue.storage.back2Me) === "true") {
                  utils.sendNotification(jsonValue.notifications.loginFailed);
                }
                else {
                  utils.sendNotification(jsonValue.notifications.cleanSession);
                }
                break;

              case 500:
              case 404:
                utils.sendNotification(jsonValue.notifications.serverError);
                break;
            }
            return $q.reject(rejection);
          }
        };
      }
    );

    localStorageServiceProvider
      .setPrefix('techlooper')
      .setStorageType('sessionStorage')
      .setNotify(true, true)
      .setStorageCookie(45);

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

    $authProvider.loginRedirect = undefined;

    $translateProvider
      .useLocalStorage()
      .useStaticFilesLoader({
        prefix: "modules/translation/messages_",
        suffix: ".json"
      })
      .registerAvailableLanguageKeys(['en', 'vi'], {
        'en-US': 'en', 'en-UK': 'en', 'en_US': 'en', 'en_UK': 'en'
      })
      .fallbackLanguage("en")
      .uniformLanguageTag('bcp47') // enable BCP-47, must be before determinePreferredLanguage!
      .determinePreferredLanguage()
      .useSanitizeValueStrategy('escaped');

    $routeProvider
      .when("/home", {
        templateUrl: "modules/home-page/home-page.tem.html",
        controller: "homePageController"
      })
      .when("/talent-profile/:text", {
        templateUrl: "modules/talent-search/home.tem.html",
        controller: "talentProfileController"
      })
      .when("/talent-search-result/:text?", {
        templateUrl: "modules/talent-search/home.tem.html",
        controller: "tsSearchResultController"
      })
      .when("/analytics/skill/:term", {
        templateUrl: "modules/technical-detail/technical-detail.tem.html",
        controller: "technicalDetailController"
      })
      .when("/companies/:companyName", {//vietnamworks
        templateUrl: "modules/talent-search/home.tem.html",
        controller: "companyProfileController"
      })
      //.when("/bubble-chart", {
      //  templateUrl: "modules/it-professional/main.tem.html",
      //  controller: "chartController"
      //})
      .when("/pie-chart", {
        templateUrl: "modules/it-professional/main.tem.html",
        controller: "chartController"
      })
      .when("/jobs/search", {
        templateUrl: "modules/it-professional/main.tem.html",
        controller: "searchFormController"
      })
      .when("/jobs/search/:text", {
        templateUrl: "modules/it-professional/main.tem.html",
        controller: "searchResultController"
      })
      //.when("/analytics/skill/:term/:period?", {
      //  templateUrl: "modules/it-professional/main.tem.html",
      //  controller: "skillAnalyticsController"
      //})
      .when("/signin", {
        templateUrl: "modules/it-professional/main.tem.html",
        controller: "signInController"
      })
      .when("/register", {
        templateUrl: "modules/it-professional/main.tem.html",
        controller: "registerController"
      })
      .when("/user", {
        templateUrl: "modules/it-professional/main.tem.html",
        controller: "userProfileController"
      })
      .when("/salary-sharing", {
        templateUrl: "modules/current-job/salary-sharing/salary-sharing.tem.html",
        controller: "salarySharingController"
      })
      .when("/salary-report", {
        templateUrl: "modules/current-job/salary-report/salary-report.tem.html",
        controller: "salaryReportController"
      })
      .when("/salary-review", {
        templateUrl: "modules/current-job/salary-review.tem.html",
        controller: "salaryReviewController"
      })
      .when("/price-job", {
        templateUrl: "modules/price-job/price-job.tem.html",
        controller: "priceJobController"
      })
      .otherwise({
        redirectTo: function () {
          if (window.location.host.indexOf("hiring") >= 0) {
            return "/home";
          }
          return "/home";
        }
      });
  }]);

techlooper.run(function (shortcutFactory, connectionFactory, loadingBoxFactory, cleanupFactory,
                         tourService, signInService, historyFactory, userService, routerService, $location,
                         utils, $rootScope, $translate, jsonValue, $location, $anchorScroll) {
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

  var doTranslate = function() {
    $translate(["newGradLevel", "experienced", "manager", "timeline", "numberOfJobs", "jobs", "isRequired", "exItSoftware", "ex149",
      "salaryRangeJob", "jobNumber", "salaryRangeInJob", "jobNumberLabel", "allLevel", "newGradLevel", "exHoChiMinh", "exManager",
      "experienced", "manager", "maximum5", "maximum3", "hasExist", "directorAndAbove", "requiredThisField",
      "genderMale", "genderFemale", "exMale", "exYob", 'exDay', 'day', 'week', 'month']).then(function (translate) {
      $rootScope.translate = translate;
    });
  }

  var campaign = $location.search();
  var langKey = (campaign && campaign.lang);
  langKey !== $translate.use() && ($translate.use(langKey));
  $rootScope.$on('$translateChangeSuccess', function () {
    langKey !== $translate.use() && ($translate.use(langKey));
    doTranslate();
  });

  doTranslate();

  $rootScope.jsonValue = jsonValue;

  //$location.hash("bodyContent");
  //$anchorScroll("#bodyContent");
});

techlooper.directive("navigation", function () {
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
  })
  .directive('onlyDigits', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ctrl) {
        function inputValue(val) {
          if (val) {
            var digits = val.replace(/[^0-9.]/g, '');

            if (digits !== val) {
              ctrl.$setViewValue(digits);
              ctrl.$render();
            }
            var number = parseFloat(digits);
            return isNaN(number) ? "" : number;
          }
          return '';
        }

        ctrl.$parsers.push(inputValue);
      }
    }
  });