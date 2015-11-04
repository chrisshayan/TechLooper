techlooper.directive("dailySummeryEmail", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/employer-dashboard/dailySummeryEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {
    }
  }
});