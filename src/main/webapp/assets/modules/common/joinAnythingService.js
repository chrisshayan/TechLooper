techlooper.factory("joinAnythingService", function (apiService, localStorageService, $location, $rootScope, $route, $translate) {

  var successfulJoinChallenge = function (joiningRegistrant) {
    var contestId = localStorageService.get("joiningChallengeId");
    var joinContests = localStorageService.get("joinContests") || "";
    joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
    var contestIdStr = "" + contestId;
    if (!_.contains(joinContests, contestIdStr)) {
      joinContests.push(contestId);
    }
    //if ($.inArray(contestId, joinContests) < 0) {
    //  joinContests.push(contestId);
    //}
    localStorageService.set("joinContests", joinContests.join(","));
    localStorageService.remove("failedJoinChallenge");
    localStorageService.remove("joiningChallengeId");
    //localStorageService.remove("firstName");
    //localStorageService.remove("lastName");
    //localStorageService.remove("email");
    localStorageService.remove("internalEmail");
    localStorageService.remove("passcode");
  }

  var instance = {
    initialize: function () {},

    joinChallenge: function(success, error) {
      var contestId = localStorageService.get("joiningChallengeId");
      if (!contestId) return;

      var firstName = localStorageService.get("firstName");
      var lastName = localStorageService.get("lastName");
      var email = localStorageService.get("email");
      var internalEmail = localStorageService.get("internalEmail");
      var passcode = localStorageService.get("passcode");
      return apiService.joinContest(contestId, firstName, lastName, email, $translate.use(), internalEmail, passcode)
        .success(function(joiningRegistrant) {
          successfulJoinChallenge(joiningRegistrant);
          _.isFunction(success) && success(joiningRegistrant);
        })
        .error(function (joiningRegistrant) {
          if (joiningRegistrant.reason == "INVALID_FBEMAIL") {
            localStorageService.set("invalidFBEmail", true);
          }
          else if (joiningRegistrant.reason == "INVALID_INTERNAL_EMAIL") {
            localStorageService.set("invalidInternalEmail", true);
          }

          localStorageService.remove("submitNow");
          _.isFunction(error) && error(joiningRegistrant);
        })
        .finally(function () {
          localStorageService.remove("joinNow");
          _.isFunction(success) || _.isFunction(error) || $route.reload();
        });
    }
  };

  var param = $location.search();
  if ($.isEmptyObject(param)) {
    return instance;
  }

  var joinAnything = localStorageService.get("joinAnything");
  var email = param.email;
  var firstName = param.firstName;
  var lastName = param.lastName;
  var id = localStorageService.get("id");

  switch (joinAnything) {
    case "webinar":
      apiService.joinWebinar(id, firstName, lastName, email)
        .success(function (webinar) {
          $rootScope.$broadcast("joinAnythingSuccess", webinar);
        });
      break;
  }

  localStorageService.remove("joinAnything");
  localStorageService.remove("id");

  return instance;
});