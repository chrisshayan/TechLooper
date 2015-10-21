techlooper.directive('challengeCriteriaInput', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/challengeCriteriaInput.html",
    scope: {
      cri: "="
    },
    link: function (scope, element, attr, ctrl) {

    }
  };
});