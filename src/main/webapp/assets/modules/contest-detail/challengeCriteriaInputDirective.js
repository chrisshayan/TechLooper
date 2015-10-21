techlooper.directive('challengeCriteriaInput', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/challengeCriteriaInput.html",
    scope: {
      cri: "=",
      contestDetail: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.criteriaForm.$setSubmitted();
      scope.contestDetail.$invalid = scope.criteriaForm.$invalid;
    }
  };
});