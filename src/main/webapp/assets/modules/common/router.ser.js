angular.module("Common").factory("routerService", function (jsonValue, utils, $location, localStorageService, historyFactory) {

  var $$ = {
    loginFailed: function () {
      $location.path(jsonValue.routerUris.signIn);
    },

    loginSuccess: function () {
      if (localStorageService.get(jsonValue.storage.back2Me) === true) {
        localStorageService.remove(jsonValue.storage.back2Me);
        var path = historyFactory.popHistory();
        $location.path(path);
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
    },

    changeUrl: function() {
      if (utils.getView() !== jsonValue.views.signIn) {
        localStorageService.remove(jsonValue.storage.back2Me);
      }
    }
  }

  utils.registerNotification(jsonValue.notifications.loginFailed, $$.loginFailed);
  utils.registerNotification(jsonValue.notifications.logoutSuccess, $$.logoutSuccess);
  utils.registerNotification(jsonValue.notifications.loginSuccess, $$.loginSuccess);
  utils.registerNotification(jsonValue.notifications.http404, $$.http404);
  utils.registerNotification(jsonValue.notifications.changeUrl, $$.changeUrl);

  var instance = {
    initialize: function () {}
  };

  return instance;
});