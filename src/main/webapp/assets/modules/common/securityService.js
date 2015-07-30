techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location) {

  //localStorage.setItem('CAPTURE-PATHS', '/');

  var instance = {
    logout: function () {
      apiService.logout()
        .success(function (data, status, headers, config) {
          $rootScope.userInfo = undefined;
        });
    },

    login: function (username, password, type) {
      var auth = (type == "social") ? {us: username, pwd: password} : {
        us: $.base64.encode(username),
        pwd: $.base64.encode(password)
      };
      return apiService.login(auth)
        .success(function (data, status, headers, config) {
          $rootScope.$broadcast("$loginSuccess");
        })
        .error(function (data, status, headers, config) {
          $rootScope.$emit("$loginFailed");
        });
    },

    getCurrentUser: function (type) {
      $rootScope.userInfo = undefined;
      return apiService.getCurrentUser(type).success(function (data) {
        $rootScope.userInfo = data;
      });
    },

    init: function () {}
  };

  return instance;
});