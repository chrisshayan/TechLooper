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
        name: localStorageService.get("firstName") + " " + localStorageService.get("lastName"),
        registrantEmail: localStorageService.get("email"),
        registrantFirstName: localStorageService.get("firstName"),
        registrantLastName: localStorageService.get("lastName")
      }

      scope.pushChallengePhase = function () {
        scope.submissionForm.$setSubmitted();
        if (scope.submissionForm.$invalid) {
          return false;
        }


        apiService.getUrlResponseCode(scope.submission.submissionURL)
          .success(function(code) {
            if (code == 200) {
              scope.submission.challengeId = scope.challenge.challengeId;
              apiService.submitMyResult(scope.submission)
                .finally(function () {
                  scope.hideSubmitForm();
                });
            }
            scope.submissionForm.submissionURL.$setValidity("invalidUrl", (code == 200));
          });


      }

      scope.hideSubmitForm = function () {
        scope.submissionForm.$setPristine();
        scope.submissionForm.$setUntouched();
        var subForm = $('.submit-phase-contest');
        subForm.removeClass('show');
        delete scope.submission.submissionURL;
        delete scope.submission.submissionDescription;
      }
    }
  }
});