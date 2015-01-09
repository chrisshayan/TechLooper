angular.module("Common").factory("userService", function (jsonValue, utils, connectionFactory, $rootScope, $q, localStorageService, $cookies) {

  var defers = {
    getUserInfo: []
  }

  var $$ = {
    notUserInfo: function() {
      $$.reject("getUserInfo");
    },

    reject: function (key, reply) {
      $.each(defers[key], function (i, d) {d.reject(reply);});
      defers[key].length = 0;
    },

    resolve: function (key, reply) {
      $.each(defers[key], function (i, d) {d.resolve(reply);});
      defers[key].length = 0;
    }
  }

  var instance = {
    initialize: function () {},

    getUserInfo: function () {
      var deferred = $q.defer();
      defers.getUserInfo.push(deferred);

      if (!instance.verifyUserSession()) {
        $$.notUserInfo();
      }
      else if (instance.notLoggedIn()) {
        connectionFactory.findUserInfoByKey();
      }
      else {
        $$.resolve("getUserInfo", $rootScope.userInfo);
      }
      return deferred.promise;
    },

    notLoggedIn: function () {
      return ($rootScope.userInfo === undefined);
    },

    verifyUserSession: function() {
      return localStorageService.cookie.get(jsonValue.storage.key) !== null;
    }
  }

  if (instance.verifyUserSession()) {
    instance.getUserInfo(); //handle refresh button: read user-info
  }
  else {
    utils.sendNotification(jsonValue.notifications.cleanSession);
  }

  utils.registerNotification(jsonValue.notifications.notUserInfo, $$.notUserInfo);
  utils.registerNotification(jsonValue.notifications.userInfo, instance.getUserInfo);
  return instance;
});