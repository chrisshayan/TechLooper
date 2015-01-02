angular.module("Navigation").directive("translation", function () {
  return {
    restrict: "E",
    replace: false,
    templateUrl: "modules/translation/translation.tem.html",
    controller: "translationController"
  }
});
