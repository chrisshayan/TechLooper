angular.module("Common", []);
angular.module("Bubble", []);
angular.module("Home", []);
angular.module("Header", []);
angular.module("Footer", []);
angular.module("Chart", []);
angular.module("Jobs", []);

var techlooper = angular.module("Techlooper", ["pascalprecht.translate", "ngResource", "ngCookies", "ngRoute", "Home", "Header", "Footer", "Common", "Chart", "Jobs"]);

techlooper.config(function($routeProvider, $translateProvider, $locationProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: "modules/translation/messages_",
        suffix: ".json"
    });

    $translateProvider.registerAvailableLanguageKeys(['en-US', 'vi']);
    $translateProvider.fallbackLanguage("en-US");
    $translateProvider.preferredLanguage("en-US");
    $translateProvider.useLocalStorage();
    //   $translateProvider.useCookieStorage();
    $translateProvider.use(window.navigator.userLanguage || window.navigator.language);

    $routeProvider.when("/", {
        templateUrl: "modules/home/home.tpl.html",
        controller: "homeController"
    }).otherwise({
        redirectTo: "/"
    });
});

techlooper.directive("header", function() {
    return {
        restrict: "A", // This mens that it will be used as an attribute and NOT as an element.
        replace: true,
        templateUrl: "modules/header/header.tpl.html",
        controller: "headerController"
    }
}).directive("footer", function() {
    return {
        restrict: "A", // This mens that it will be used as an attribute and NOT as an element.
        replace: true,
        templateUrl: "modules/footer/footer.tpl.html"
        //controller : "footerController"
    }
});