techlooper.directive("getPromotedForm", function ($http) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html",
    link: function (scope, element, attr, ngModel) {
    }
  }
});