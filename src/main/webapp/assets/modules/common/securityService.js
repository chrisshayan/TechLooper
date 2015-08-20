techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location, localStorageService) {

  //localStorage.setItem('CAPTURE-PATHS', '/');

  var instance = {
    logout: function () {

      var view = utils.getView();
      switch (view) {
        case jsonValue.views.freelancerPostProject:
        case jsonValue.views.employerDashboard:
        case jsonValue.views.postChallenge:
        case jsonValue.views.createEvent:
          break;

        default:
          localStorageService.set("lastFoot", $location.path());
          break;
      }

      apiService.logout()
        .success(function (data, status, headers, config) {
          localStorageService.remove("social");
          $rootScope.userInfo = undefined;

          switch (view) {
            case jsonValue.views.freelancerPostProject:
            case jsonValue.views.employerDashboard:
            case jsonValue.views.postChallenge:
            case jsonValue.views.createEvent:
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

    getCurrentUser: function (type) {
      if ($rootScope.userInfo) {
        var deferred = $q.defer();
        deferred.resolve($rootScope.userInfo);
        return deferred.promise;
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

    login: function (username, password, type) {
      var auth = (type == "social") ? {us: username, pwd: password} : {
        us: $.base64.encode(username),
        pwd: $.base64.encode(password)
      };
      return apiService.login(auth)
        .success(function (data, status, headers, config) {
          if (localStorageService.get("social")) {
            var type = "social";
          }
          instance.getCurrentUser(type).success(function () {
            instance.routeByRole();
          });
        })
        .error(function (data, status, headers, config) {
          $rootScope.$emit("$loginFailed");
        });
    },

    routeByRole: function () {
      if (!$rootScope.userInfo) return;

      //var protectedPage = localStorageService.get("protectedPage");
      //if (protectedPage) {
      //  localStorageService.remove("protectedPage");
      //  return $location.url(protectedPage);
      //}

      var lastFoot = localStorageService.get("lastFoot");
      if (lastFoot) {
        localStorageService.remove("lastFoot");
        if (lastFoot !== "/login" && lastFoot !== "/user-type") {
          return $location.url(lastFoot);
        }
      }

      switch ($rootScope.userInfo.roleName) {
        case "EMPLOYER":
          return $location.path("/employer-dashboard");

        case "JOB_SEEKER":
          return $location.path("/home");
      }
    },

    removeProtectedLastFoot: function () {
      var path = localStorageService.get("protectedPage");
      if (instance.isProtectedPage(path)) {
        localStorageService.remove("protectedPage");
      }
    },

    isProtectedPage: function(path) {
      //var path = localStorageService.get("protectedPage");
      switch (utils.getView(path)) {
        case jsonValue.view.freelancerPostProject:
          return "/freelancer/post-project";

        case jsonValue.view.employerDashboard:
          return "/employer-dashboard";

        case jsonValue.view.createEvent:
          return "/post-event";

        case jsonValue.view.postChallenge:
          return "/freelancer/post-project";
      }

      return false;
    },

    initialize: function () {
      $rootScope.$on("$routeChangeStart", function (event, next, current) {
        //var path = instance.isProtectedPage();

        var view = utils.getView();
        switch (view) {
          case jsonValue.views.freelancerPostProject:
            var lastPage = "/freelancer/post-project";
          case jsonValue.views.employerDashboard:
            var lastPage = "/employer-dashboard";
          case jsonValue.views.createEvent:
            var lastPage = "/post-event";
          case jsonValue.views.postChallenge:
            if ($rootScope.userInfo && $rootScope.userInfo.roleName !== "EMPLOYER") {
              alert("Your current account is not authorized to access that feature. Please use your VietnamWorks employer account instead.");
              return event.preventDefault();
            }
            else if (!$rootScope.userInfo) {
              //localStorageService.set("protectedPage", lastPage || "/post-challenge");
              instance.getCurrentUser().error(function () {
                localStorageService.set("lastFoot", $location.path());
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
            return localStorageService.set("lastFoot", $location.url());
        }
      });
    }
  };

  return instance;
});