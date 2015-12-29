techlooper.directive("signInInternalChallenge", function (apiService, $translate, $location, $route, localStorageService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/challenge/signInInternalChallenge.html",
    link: function (scope, el, attrs) {
    }
  }
});