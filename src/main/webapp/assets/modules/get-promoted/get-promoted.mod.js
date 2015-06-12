techlooper.directive("getPromotedForm", function ($http, $location, utils, jsonValue, vnwConfigService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html"
  }
});