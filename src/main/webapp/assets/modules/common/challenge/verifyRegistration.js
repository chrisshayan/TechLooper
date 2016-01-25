techlooper.directive("verifyRegistration", function (apiService, $translate, $location, $route, localStorageService) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "=",
      cancel: "="
    },
    templateUrl: "modules/common/challenge/verifyRegistration.html",
    link: function (scope, el, attrs) {
    }
  }
});