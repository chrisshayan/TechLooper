techlooper.directive("verifyEmailDomain", function (apiService, $translate, $location, $route, localStorageService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/challenge/verifyEmailDomain.html",
    link: function (scope, el, attrs) {
    }
  }
});