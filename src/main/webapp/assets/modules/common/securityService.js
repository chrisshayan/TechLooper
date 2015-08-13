techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location, localStorageService) {

  //localStorage.setItem('CAPTURE-PATHS', '/');

  var instance = {
    logout: function () {
      return apiService.logout()
        .success(function (data, status, headers, config) {
          $rootScope.userInfo = undefined;
          return $location.path("/");
        });
    },

    login: function (username, password, type) {
      var auth = (type == "social") ? {us: username, pwd: password} : {
        us: $.base64.encode(username),
        pwd: $.base64.encode(password)
      };
      return apiService.login(auth)
        .success(function (data, status, headers, config) {
          //$rootScope.$broadcast("$loginSuccess");

          //var protectedPage = localStorageService.get("protectedPage");
          //if (protectedPage) {
          //  localStorageService.remove("protectedPage");
          //  return $location.url(protectedPage);
          //}
          instance.getCurrentUser();

        })
        .error(function (data, status, headers, config) {
          $rootScope.$emit("$loginFailed");
        });
    },

    routeByRole: function() {
      if (!$rootScope.userInfo) return;

      var protectedPage = localStorageService.get("protectedPage");
      if (protectedPage) {
        localStorageService.remove("protectedPage");
        return $location.url(protectedPage);
      }

      switch ($rootScope.userInfo.roleName) {
        case "EMPLOYER":
          return $location.path("/employer-dashboard");

        case "JOB_SEEKER":
          return $location.path("/home");
      }
    },

    getCurrentUser: function (type) {
      if ($rootScope.userInfo) {
        return $rootScope.userInfo;
      }

      $rootScope.userInfo = undefined;
      utils.sendNotification(jsonValue.notifications.loading, $(window).height());
      return apiService.getCurrentUser(type)
        .success(function (data) {
          utils.sendNotification(jsonValue.notifications.loaded, $(window).height());

          $rootScope.userInfo = data;

          var lastFoot = localStorageService.get("lastFoot");
          if (lastFoot && ["/login", "/user-type"].indexOf(lastFoot) == -1) {
            localStorageService.remove("lastFoot");
            return $location.path(lastFoot);
          }
          localStorageService.remove("lastFoot");

          instance.routeByRole();
        })
        .error(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});
    },

    init: function () {}
  };

  return instance;
});