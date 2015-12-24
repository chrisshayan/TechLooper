techlooper.directive('challengeDetailsTab', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/details/challengeDetailsTab.html",
    link: function (scope, element, attr, ctrl) {
      scope.$on("challenge-criteria-saved", function (e, criteria) {
        scope.$eventName = "challenge-criteria-saved";
      });
    }
  };
})