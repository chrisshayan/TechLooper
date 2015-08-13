techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location, localStorageService) {

  //localStorage.setItem('CAPTURE-PATHS', '/');

  var instance = {
    logout: function () {

      var view = utils.getView();
      switch (view) {
        case jsonValue.views.freelancerPostProject:
        case jsonValue.views.employerDashboard:
        case jsonValue.views.postChallenge:
          break;

        default:
          localStorageService.set("lastFoot", $location.path());
          break;
      }

      apiService.logout()
        .success(function (data, status, headers, config) {
          $rootScope.userInfo = undefined;

          switch (view) {
            case jsonValue.views.freelancerPostProject:
            case jsonValue.views.employerDashboard:
            case jsonValue.views.postChallenge:
              break;

            default:
              var lastFoot = localStorageService.get("lastFoot");
              if (lastFoot) {
                return $location.url(lastFoot);
              }
          }


          return $location.path("/");
        })
        .finally(function () {localStorageService.remove("lastFoot");});
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

    routeByRole: function () {
      if (!$rootScope.userInfo) return;

      var protectedPage = localStorageService.get("protectedPage");
      if (protectedPage) {
        localStorageService.remove("protectedPage");
        return $location.url(protectedPage);
      }

      var lastFoot = localStorageService.get("lastFoot");
      if (lastFoot) {
          localStorageService.remove("lastFoot");
          return $location.url(lastFoot);
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

          //var lastFoot = localStorageService.get("lastFoot");
          //if (lastFoot && ["/login", "/user-type"].indexOf(lastFoot) == -1) {
          //  localStorageService.remove("lastFoot");
          //  return $location.path(lastFoot);
          //}
          //localStorageService.remove("lastFoot");

          //instance.routeByRole();
        })
        .error(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});
    },

    removeProtectedLastFoot: function () {
      var path = localStorageService.get("protectedPage");
      if (/\/freelancer\/post-project/.test(path) || /\/employer-dashboard/.test(path) || /\/post-challenge/.test(path)) {
        localStorageService.remove("protectedPage");
      }
    },

    initialize: function () {
      $rootScope.$on("$routeChangeStart", function (event, next, current) {
        var view = utils.getView();
        switch (view) {
          case jsonValue.views.freelancerPostProject:
            var lastPage = "/freelancer/post-project";
          case jsonValue.views.employerDashboard:
            var lastPage = "/employer-dashboard";
          case jsonValue.views.postChallenge:
            if ($rootScope.userInfo && $rootScope.userInfo.roleName !== "EMPLOYER") {
              alert("no permission");
              return event.preventDefault();
            }
            else if (!$rootScope.userInfo) {
              localStorageService.set("protectedPage", lastPage || "/post-challenge");
              instance.getCurrentUser().error(function () {
                localStorageService.set("lastFoot", $location.url());
                return $location.path("/login");
              });
            }
            break;

          case jsonValue.views.userType:
          case jsonValue.views.login:
            if (current) {
              localStorageService.set("lastFoot", current.$$route.originalPath);
            }
            if ($rootScope.userInfo) {
              return event.preventDefault();
            }
            break;

          default:
            return localStorageService.set("lastFoot", $location.path());
        }
      });
    }
  };

  return instance;
});