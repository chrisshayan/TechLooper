techlooper.directive("submissionChallenge", function (localStorageService, apiService) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "="
    },
    templateUrl: "modules/common/challenge/submissionChallenge.html",
    link: function (scope, el, attrs) {

      scope.submission = {
        challengeId: scope.challenge ? scope.challenge.challengeId : "",
        name: localStorageService.get("firstName") + " " + localStorageService.get("lastName"),
        registrantEmail: localStorageService.get("email"),
        registrantFirstName: localStorageService.get("firstName"),
        registrantLastName: localStorageService.get("lastName")
      }

      scope.pushChallengePhase = function(){
        scope.submissionForm.$setSubmitted();
        if (scope.submissionForm.$invalid) {
          return false;
        }

        apiService.submitMyResult(scope.submission);
        scope.submission = {
          challengeId: scope.challenge.challengeId,
          name: localStorageService.get("firstName") + " " + localStorageService.get("lastName"),
          registrantEmail: localStorageService.get("email"),
          registrantFirstName: localStorageService.get("firstName"),
          registrantLastName: localStorageService.get("lastName")
        }
        scope.hideSubmitForm();
      }

      scope.hideSubmitForm = function(){
        scope.submissionForm.$setPristine();
        scope.submissionForm.$setUntouched();
        var subForm = $('.submit-phase-contest');
        subForm.find('#txtDescription').val('');
        subForm.find('#txtSubmissionURL').val('');
        subForm.removeClass('show');
        delete scope.submissionURL;
        delete scope.submissionDescription;
      }
    }
  }
});