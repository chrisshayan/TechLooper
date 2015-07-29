techlooper.factory("securityService", function (apiService, $rootScope, $q, utils, jsonValue, $location) {

  //localStorage.setItem('CAPTURE-PATHS', '/');

  var instance = {
    //logout: function () {
    //  apiService.logout()
    //    .success(function (data, status, headers, config) {
    //      $rootScope.userInfo = undefined;
    //      $location.path("/");
    //    });
    //},

    login: function (username, password, type) {
      var auth = (type == "social") ? {us: username, pwd: password} : {us: $.base64.encode(username), pwd: $.base64.encode(password)};
      apiService.login(auth)
        .success(function (data, status, headers, config) {
          $rootScope.$emit("$loginSuccess");
        })
        .error(function (data, status, headers, config) {
          $rootScope.$emit("$loginFailed");
        });
    },

    getCurrentUser: function () {
      var deffer = $q.defer();
      apiService.getCurrentUser()
        .success(function (data) {
          $rootScope.userInfo = data;
          deffer.resolve(data);
        })
        .catch(function () {deffer.reject();});
      return deffer.promise;
    },

    init: function () {}
  };

  return instance;
});