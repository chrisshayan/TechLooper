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
          $.removeCookie("JSESSIONID");
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
      var auth = type ? {us: username, pwd: password} : {
        us: $.base64.encode(username),
        pwd: $.base64.encode(password)
      };
      return apiService.login(auth)
        .success(function (data, status, headers, config) {
          instance.getCurrentUser().success(function () {
            instance.routeByRole();
          });
        });
    },

    routeByRole: function () {
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

    initialize: function () {
      $rootScope.$on("$locationChangeSuccess", function (event, next, current) {
        console.log(456);
        $rootScope.currentUiView = utils.getUiView();
        if ($rootScope.currentUiView.name == "rootPage") {
          return false;
        }

        var param = $location.search();
        var notHasParam = !$.isEmptyObject(param);

        var notSignedIn = !$rootScope.userInfo && $rootScope.currentUiView.type !== "LOGIN";
        if (notSignedIn && notHasParam) {// should keep last print
          localStorageService.set("lastFoot", $location.url());
        }
      });

      $rootScope.$on("$routeChangeSuccess", function (event, next, current) {
        console.log(123);
        var isSignInView = $rootScope.currentUiView.type == "LOGIN";
        var fromPath =  current && current.$$route && current.$$route.originalPath;
        var shouldKeepPreviousFoot = isSignInView && fromPath;
        if (shouldKeepPreviousFoot) {// should keep last print
          localStorageService.set("lastFoot", fromPath);
        }
      });

      $rootScope.$on("$routeChangeStart", function (event, next, current) {
        var uiView = utils.getUiView();
        var roles = uiView.roles || [];
        if ($rootScope.userInfo) {
          var noPermission = roles.length > 0 && roles.indexOf($rootScope.userInfo.roleName) < 0;
          if (noPermission) {
            alert("Your current account is not authorized to access that feature. Please use your VietnamWorks employer account instead.");
            return event.preventDefault();
          }
          return false;
        }

        if (roles.length > 0) {//is protected pages
          instance.getCurrentUser().error(function (userInfo) {
            return $location.path(uiView.loginUrl);
          });
        }

      });
    }
  };

  return instance;
});