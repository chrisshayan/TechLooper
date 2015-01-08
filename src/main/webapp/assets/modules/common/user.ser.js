angular.module("Common").factory("userService", function (jsonValue, utils, connectionFactory, $rootScope, $q) {

  var defers = {
    getUserInfo: []
  }

  var instance = {
    initialize: function () {},

    getUserInfo: function () {
      var deferred = $q.defer();
      defers.getUserInfo.push(deferred);
      //TODO: check actual user logged-in
      if ($rootScope.userInfo === undefined) {
        connectionFactory.findUserInfoByKey();
      }
      else {
        $.each(defers.getUserInfo, function (i, d) {d.resolve($rootScope.userInfo);});
        defers.getUserInfo.length = 0;
      }
      return deferred.promise;
    },

    notLoggedIn: function() {
      return ($rootScope.userInfo === undefined);
    }
  }
  instance.getUserInfo();//read userInfo
  utils.registerNotification(jsonValue.notifications.userInfo, instance.getUserInfo);
  return instance;
});