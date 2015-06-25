techlooper.factory("apiService", function ($rootScope, $location, jsonValue, $http) {
  var instance = {

    login: function (techlooperKey) {
      return $http.post("login",
        $.param(techlooperKey), {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}});
    },

    /**
     * Get current login user info
     * */
    getCurrentUser: function () {
      return $http.get("user/current");
    },

    logout: function () {
      return $http.get("logout");
    }
  }

  return instance;
});