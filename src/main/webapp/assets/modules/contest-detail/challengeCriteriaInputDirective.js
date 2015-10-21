techlooper.directive('challengeCriteriaInput', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/challengeCriteriaInput.html",
    scope: {
      cri: "=",
      criteriaInvalid: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.criteriaForm.$setSubmitted();
      if(scope.criteriaForm.$invalid) {
        scope.criteriaInvalid = false
      }
    }
  };
});