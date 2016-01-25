techlooper.directive("verifyEmailDomain", function (apiService, $translate, $location, $route, localStorageService) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "=",
      cancel: "="
    },
    templateUrl: "modules/common/challenge/verifyEmailDomain.html",
    link: function (scope, el, attrs) {
    }
  }
});