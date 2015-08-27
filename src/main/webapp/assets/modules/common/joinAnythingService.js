techlooper.factory("joinAnythingService", function (apiService, localStorageService, $location, $rootScope) {

  var instance = {
    initialize: function () {}
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
          $rootScope.$broadcast("joinAnything", webinar);
        });
      break;
  }

  localStorageService.remove("joinAnything");
  localStorageService.remove("id");

  return instance;
});