techlooper.directive('challengeEvaluationCriteriaInput', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/evaluation-criteria/evaluationCriteriaInput.html",
      scope: {
        cri: "=",
        contestDetail: "="
      },
      link: function (scope, element, attr, ctrl) {
      }
    };
  });