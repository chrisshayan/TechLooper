techlooper.factory("joinAnythingService", function (apiService, localStorageService, $location, $rootScope) {

  var instance = {
    initialize: function () {}
  };

  var email = localStorageService.get("email");
  if (!email) {
    $rootScope.$broadcast("joinAnythingWithoutEmail");
  }

  var param = $location.search();
  if ($.isEmptyObject(param) || !email) {
    return instance;
  }

  var joinAnything = localStorageService.get("joinAnything");
  var firstName = localStorageService.get("firstName");
  var lastName = localStorageService.get("lastName");
  var id = localStorageService.get("id");

  switch (joinAnything) {
    case "webinar":
      apiService.joinWebinar(id, firstName, lastName, email)
        .success(function (webinar) {
          //var joinEvents = localStorageService.get("joinEvents") || [];
          //joinEvents.push(id);
          //localStorageService.set("joinEvents", joinEvents);
          $rootScope.$broadcast("joinAnything", webinar);
        });
      break;
  }

  localStorageService.remove("joinForm");
  localStorageService.remove("id");

  return instance;
});