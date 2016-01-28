techlooper.directive("joinInternalChallenge", function (apiService, $translate, $location, $route, localStorageService) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "=",
      cancel: "="
    },
    templateUrl: "modules/common/challenge/joinInternalChallenge.html",
    link: function (scope, el, attrs) {
      var savedDraftRegistrant = localStorageService.get("savedDraftRegistrant");
      savedDraftRegistrant && apiService.getDraftRegistrant(savedDraftRegistrant)
        .success(function(draft) {
          scope.registrant = draft;
        });

      scope.joinInternalForm.email.$validators.domainMatch = function (modelValue, viewValue) {
        if (!modelValue) return true;
        if (modelValue.length == 0) return true;
        if (scope.joinInternalForm.email.$error.email) return true;

        var valid = false;
        $.each(scope.challenge.companyDomains, function (i, companyDomain) {
          return !(valid = new RegExp("( |^)[^ ]*@[a-zA-Z0-9.]*" + companyDomain + "( |$)").test(modelValue));
        });
        return valid;
      };

      scope.sentVerifyEmail = function() {
        var firstName = localStorageService.get("firstName");
        var lastName = localStorageService.get("lastName");
        var email = localStorageService.get("email");
        apiService.saveDraftRegistrant(scope.challenge.challengeId, firstName, lastName, email, scope.registrant.registrantInternalEmail, $translate.use())
          .success(function(draft) {
            console.log(draft);
            scope.registrant.$sentVerifyEmail = true;
            delete scope.registrant.registrantInternalEmail;
          });
      }

      scope.joinChallenge = function () {
        scope.joinInternalForm.$setSubmitted();
        if (scope.joinInternalForm.$invalid) return;
        //localStorageService.set("lastName", scope.registrant.registrantLastName);
        //localStorageService.set("firstName", scope.registrant.registrantFirstName);
        //localStorageService.set("email", scope.registrant.registrantEmail);
        localStorageService.set("joiningChallengeId", scope.challenge.challengeId);
        localStorageService.set("priorFoot", $location.url());
        localStorageService.set("lastFoot", $location.url());
        localStorageService.set("joinNow", true);
        $route.reload();
        //joinChallenge();
        //apiService.joinContest(scope.challenge.challengeId, scope.registrant.firstName,
        //  scope.registrant.lastName, $translate.use());
      };

      scope.doCancel = function () {
        scope.registrant = {firstName: "", lastName: "", email: ""};
        scope.joinInternalForm.$setPristine();
        _.isFunction(scope.cancel) && scope.cancel(scope.challenge);
      };
    }
  }
});