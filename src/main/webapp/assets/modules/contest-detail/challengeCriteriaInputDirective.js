techlooper.directive('challengeCriteriaInput', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/challengeCriteriaInput.html",
    scope: {
      cri: "=",
      contestDetail: "="
//=======
//      criteriaInvalid: "="
//>>>>>>> 236c93a615e8bee46fe165a4eb60d57d706cba3f
    },
    link: function (scope, element, attr, ctrl) {
      scope.criteriaForm.$setSubmitted();
      if(scope.criteriaForm.$invalid) {
        scope.criteriaInvalid = false
      }
    }
  };
});