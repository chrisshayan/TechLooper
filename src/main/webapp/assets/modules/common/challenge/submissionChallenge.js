techlooper.directive("submissionChallenge", function (localStorageService, apiService, $timeout, $rootScope) {
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
        $('.feedback-loading').css('visibility', 'inherit');
        apiService.getUrlResponseCode(scope.submission.submissionURL)
          .success(function (code) {
            var valid = (code >= 200) && (code < 400);
            if (valid) {
              scope.submission.challengeId = scope.challenge.challengeId;
              apiService.submitMyResult(scope.submission)
                .success(function(data) {
                  var submission = data;
                  $rootScope.$broadcast("success-submission-challenge", submission);
                })
                .finally(function () {
                  scope.hideSubmitForm();
                });
            }
            scope.submissionForm.submissionURL.$setValidity("invalidUrl", valid);
          })
          .finally(function () {
            $timeout(function(){
              $('.feedback-loading').css('visibility', 'hidden');
              scope.cancel();
            }, 500);
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