angular.module("Common").factory("routerService", function (jsonValue, utils, $location, localStorageService, historyFactory) {

  var $$ = {
    loginFailed: function () {
      $location.path(jsonValue.routerUris.signIn);
    },

    loginSuccess: function () {
      if (localStorageService.get(jsonValue.storage.back2Me) === true) {
        localStorageService.remove(jsonValue.storage.back2Me);
        $location.path(historyFactory.popHistory());
      }
      else {//default after sign-in
        $location.path(jsonValue.routerUris.register);
      }
    },

    http404: function () {
      $location.path("/");
    },

    logoutSuccess: function() {
      $location.path("/");
    }
  }

  utils.registerNotification(jsonValue.notifications.loginFailed, $$.loginFailed);
  utils.registerNotification(jsonValue.notifications.logoutSuccess, $$.logoutSuccess);
  utils.registerNotification(jsonValue.notifications.loginSuccess, $$.loginSuccess);
  utils.registerNotification(jsonValue.notifications.http404, $$.http404);

  var instance = {
    initialize: function () {}
  };

  return instance;
});