angular.module("Common").factory("routerService", function (jsonValue, utils, $location, localStorageService, historyFactory) {

  var $$ = {
    loginFailed: function () {
      $location.path(jsonValue.routerUris.signIn);
    },

    loginSuccess: function () {
      if (localStorageService.get(jsonValue.storage.back2Me) === "true") {
        var path = historyFactory.popHistory();
        $location.path(path);
      }
      else {//default after sign-in
        $location.path(jsonValue.routerUris.register);
      }
    },

    serverError: function () {
      console.log("Unable to connect to server");
    },

    logoutSuccess: function() {
      $location.path("/");
    }
  }

  utils.registerNotification(jsonValue.notifications.loginFailed, $$.loginFailed);
  utils.registerNotification(jsonValue.notifications.logoutSuccess, $$.logoutSuccess);
  utils.registerNotification(jsonValue.notifications.loginSuccess, $$.loginSuccess);
  utils.registerNotification(jsonValue.notifications.serverError, $$.serverError);

  var instance = {
    initialize: function () {}
  };

  return instance;
});