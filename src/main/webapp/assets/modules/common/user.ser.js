angular.module("Common").factory("userService", function (jsonValue, utils, connectionFactory, $rootScope, $q) {

  var defers = {
    getUserInfo: []
  }

  var instance = {
    initialize: function () {},

    getUserInfo: function () {
      var deferred = $q.defer();
      defers.getUserInfo.push(deferred);

      if (instance.notLoggedIn()) {
        connectionFactory.findUserInfoByKey();
      }
      else {
        instance.resolve("getUserInfo", $rootScope.userInfo);
      }
      return deferred.promise;
    },

    resolve: function (key, reply) {
      $.each(defers[key], function (i, d) {d.resolve(reply);});
      defers[key].length = 0;
    },

    //TODO: check actual user logged-in
    notLoggedIn: function () {
      return ($rootScope.userInfo === undefined);
    }
  }

  instance.getUserInfo(); //handle refresh button: read user-info
  utils.registerNotification(jsonValue.notifications.userInfo, instance.getUserInfo);
  return instance;
});