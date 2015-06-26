techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location) {

  var capturePaths = [];

  var instance = {
    //logout: function () {
    //  apiService.logout()
    //    .success(function (data, status, headers, config) {
    //      $rootScope.userInfo = undefined;
    //      $location.path("/");
    //    });
    //},

    login: function (username, password) {
      $.cookie("us", $.base64.encode(username));
      $.cookie("pwd", $.base64.encode(password));

      apiService
        .login({us: $.cookie("us"), pwd: $.cookie("pwd")})
        .success(function (data, status, headers, config) {$rootScope.$emit("$loginSuccess");});

      $.removeCookie("us");
      $.removeCookie("pwd");
    },

    getCurrentUser: function () {
      var deffer = $q.defer();
      apiService.getCurrentUser().success(function(data) {
        $rootScope.userInfo = data;
        deffer.resolve(data);
      });
      return deffer.promise;
    },

    ableToGo: function () {
      instance.getCurrentUser()
        .then(function (data) {
          var path = capturePaths.pop();
          if (path) {
            return $location.path(path);
          }
          return $location.path("/");
        })
        .catch(function () {return $location.path("/login");});
    }

    //init: function () {
    //  switch (utils.getView()) {
    //    case jsonValue.views.login:
    //    case jsonValue.views.postContest:
    //      instance.ableToGo();
    //      break;
    //  }
    //}
  };

  $rootScope.$on("$loginSuccess", function (e, newUrl, oldUrl, newState, oldState) {
    instance.ableToGo();
  });

  $rootScope.$on("$locationChangeStart", function (event, next, current) {
    switch (utils.getView()) {
      case jsonValue.views.postContest:
        capturePaths.push("/post-contest");
      case jsonValue.views.login:
        instance.ableToGo();
        break;
    }
  });

  return instance;
});