techlooper.controller("navigationController", function ($scope, securityService, apiService, localStorageService, $location,
                                                  jsonValue, utils, $timeout, $rootScope) {
  $scope.logout = function() {
    securityService.logout();
  }

  var code = localStorageService.get("code");
  if (code) {
    utils.sendNotification(jsonValue.notifications.loading, $(window).height());
    securityService.login(code, localStorageService.get("social"), "social").success(function (data) {
      securityService.getCurrentUser("social");
    }).finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});
    localStorageService.remove("code");
    return;
  }

  //securityService.getCurrentUser("social");

  $scope.loginBySocial = function (provider) {
    apiService.getSocialLoginUrl(provider).success(function (url) {
      localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }

});
