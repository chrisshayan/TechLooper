techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location, localStorageService) {

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

        var lastFoot = localStorageService.get("lastFoot");
        if (lastFoot && ["/login", "/user-type"].indexOf(lastFoot) == -1) {
          localStorageService.remove("lastFoot");
          return $location.path(lastFoot);
        }
        localStorageService.remove("lastFoot");

        switch (data.roleName) {
          case "EMPLOYER":
            return $location.path("/hiring");

          case "JOB_SEEKER":
            return $location.path("/home");
        }
      });
    },

    init: function () {}
  };

  return instance;
});