techlooper.directive("joinInternalChallenge", function (apiService, $translate, $location, $route, localStorageService, joinAnythingService) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "=",
      cancel: "="
    },
    templateUrl: "modules/common/challenge/joinInternalChallenge.html",
    link: function (scope, el, attrs) {
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
      scope.sentVerifyEmail = function () {
        scope.joinInternalForm.$setSubmitted();
        if (scope.joinInternalForm.$invalid) return;

        var firstName = localStorageService.get("firstName");
        var lastName = localStorageService.get("lastName");
        var email = localStorageService.get("email");
        apiService.saveDraftRegistrant(scope.challenge.challengeId, firstName, lastName, email, scope.registrant.registrantInternalEmail, $translate.use())
          .success(function (draft) {
            scope.registrant.$sentVerifyEmail = true;
          });
      }

      scope.joinChallenge = function () {
        scope.joinInternalForm.passcode.$setValidity("matchPasscode", true);
        scope.joinInternalForm.passcode.$setValidity("singleAccount", true);
        scope.joinInternalForm.$setSubmitted();
        if (scope.joinInternalForm.$invalid) return;

        localStorageService.set("joiningChallengeId", scope.challenge.challengeId);
        localStorageService.set("internalEmail", scope.registrant.registrantInternalEmail);
        localStorageService.set("passcode", scope.registrant.passcode);
        joinAnythingService.joinChallenge(function() {
          localStorageService.remove("joiningChallengeId");
          $route.reload();
        }, function(joiningRegistrant) {
          //scope.challenge.$failJoin = true;
          //if (joiningRegistrant.reason == "INVALID_FBEMAIL") {
          //  scope.challenge.$failedJoin = true;
          //}
          joiningRegistrant.reason == "UNMATCH_PASSCODE" && scope.joinInternalForm.passcode.$setValidity("matchPasscode", false);
          joiningRegistrant.reason == "SINGLE_ACCOUNT" && scope.joinInternalForm.passcode.$setValidity("singleAccount", false);
          localStorageService.remove("joiningChallengeId");
        });


        //apiService.joinContest(scope.challenge.challengeId, localStorageService.get("lastName"),
        //  localStorageService.get("firstName"), localStorageService.get("email"), $translate.use(),
        //  scope.registrant.registrantInternalEmail, scope.registrant.passcode)
        //  .success(function (joiningRegistrant) {
        //    var joinContests = localStorageService.get("joinContests") || "";
        //    joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
        //    if ($.inArray(scope.challenge.challengeId, joinContests) < 0) {
        //      joinContests.push(scope.challenge.challengeId);
        //    }
        //    localStorageService.set("joinContests", joinContests.join(","));
        //    scope.doCancel();
        //    $route.reload();
        //  })
        //  .error(function() {
        //    scope.joinInternalForm.$setValidity("matchPasscode", false);
        //  });
      };

      scope.doCancel = function () {
        scope.registrant = {firstName: "", lastName: "", email: ""};
        scope.joinInternalForm.$setPristine();
        //localStorageService.remove("joinNowInternalChallenge");
        //localStorageService.remove("joiningChallengeId");
        //localStorageService.remove("failedJoin");
        _.isFunction(scope.cancel) && scope.cancel(scope.challenge);
      };
      $("input.inputText").keypress(function(event) {
        if (event.which == 13) {
          event.preventDefault();
          if(!scope.registrant.$sentVerifyEmail){
            scope.sentVerifyEmail();
          }else{
            scope.joinChallenge();
          }

        }
      });
    }
  }
});