techlooper.directive("submissionChallenge", function ($timeout, jsonValue, utils) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      ngModel: "="
    },
    templateUrl: "modules/common/challenge/submissionChallenge.html",
    link: function (scope, el, attrs) {
      scope.pushChallengePhase = function(){
        scope.submissionForm.$setSubmitted();
        if (scope.submissionForm.$invalid) {
          return false;
        }
        console.log(scope.submissionDescription);
        console.log(scope.submissionURL);
      }
      scope.hideSubmitForm = function(){
        var subForm = $('.submit-phase-contest');
        subForm.find('#txtDescription').val('');
        subForm.find('#txtSubmissionURL').val('');
        subForm.removeClass('show');
      }
    }
  }
});