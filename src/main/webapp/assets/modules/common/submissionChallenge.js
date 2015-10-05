techlooper.directive("submissionChallenge", function ($timeout, jsonValue, utils) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      ngModel: "="
    },
    templateUrl: "modules/common/submission-challenge.html",
    link: function (scope, el, attrs) {
      scope.pushChallengePhase = function(){
        scope.submissionForm.$setSubmitted();
        if (scope.submissionForm.$invalid) {
          return false;
        }
        console.log(scope.submissionDescription);
        console.log(scope.submissionURL);
      }
    }
  }
});