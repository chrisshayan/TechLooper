techlooper.directive("submissionChallenge", function (localStorageService, apiService, $timeout, $rootScope, $location) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "=",
      form: "="
    },
    templateUrl: "modules/common/challenge/submissionChallenge.html",
    link: function (scope, el, attrs) {
      scope.form = {};
      delete scope.loadingData;
      scope.form.hideSubmitForm = function () {
        if (scope.submissionForm) {
          scope.submissionForm.$setPristine();
          scope.submissionForm.$setUntouched();
        }
        delete scope.visibleSubmitForm;
        delete scope.submission.submissionURL;
        delete scope.submission.submissionDescription;
      }

      scope.form.showSubmitForm = function () {
        localStorageService.set("submitNow", scope.challenge.challengeId);
        if (scope.challenge.$isPublic) {
          apiService.joinNowByFB();
        }
        else {
          handleChallengeChanged();
        }
      }


      var handleChallengeChanged = function () {
        var challengeId = localStorageService.get("submitNow");
        if (!scope.challenge) return;
        if (scope.challenge && (challengeId == scope.challenge.challengeId)) {
          localStorageService.remove("submitNow");
          localStorageService.remove("joinNow");

          var firstName = localStorageService.get("firstName");
          var lastName = localStorageService.get("lastName");
          var email = localStorageService.get("email");

          apiService.findRegistrantActivePhase(challengeId, email)
            .success(function (phase) {
              scope.submission.submissionPhase = phase;
            });

          scope.visibleSubmitForm = true;
        }

        if (scope.challenge.$isPublic) {
          scope.submission = {
            name: localStorageService.get("firstName") + " " + localStorageService.get("lastName"),
            registrantEmail: localStorageService.get("email"),
            registrantFirstName: localStorageService.get("firstName"),
            registrantLastName: localStorageService.get("lastName")
          }
        }
        else {
          scope.submission = {};
        }

        if (scope.challenge.$isInternal && scope.submissionForm && scope.submissionForm.internalEmail) {
          scope.submissionForm.internalEmail.$validators.domainMatch = function (modelValue, viewValue) {
            if (!modelValue) return true;
            if (modelValue.length == 0) return true;
            if (scope.submissionForm.internalEmail.$error.email) return true;

            var valid = false;
            $.each(scope.challenge.companyDomains, function (i, companyDomain) {
              return !(valid = new RegExp("( |^)[^ ]*@[a-zA-Z0-9.]*" + companyDomain + "( |$)").test(modelValue));
            });
            return valid;
          };
        }
      }
      scope.$watch("challenge", function () {
        handleChallengeChanged();
      });

      scope.pushChallengePhase = function () {
        scope.submissionForm.$setSubmitted();
        if (scope.submissionForm.$invalid) {
          return false;
        }
        scope.loadingData = true;
        $location.search({});
        apiService.getUrlResponseCode(scope.submission.submissionURL)
          .success(function (code) {
            var inValid = (code == 404);
            if (!inValid) {
              scope.submission.challengeId = scope.challenge.challengeId;
              apiService.submitMyResult(scope.submission)
                .success(function (data) {
                  var submission = data;
                  $rootScope.$broadcast("submission-accepted", submission);
                  scope.form.hideSubmitForm();
                })
                .error(function () {
                  scope.submissionForm.submissionPassCode.$setValidity("credential", false);
                });
            }
            scope.submissionForm && scope.submissionForm.submissionURL.$setValidity("invalidUrl", !inValid);
          });
      }
    }
  }
});