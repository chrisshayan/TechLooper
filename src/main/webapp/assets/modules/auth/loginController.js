techlooper.controller("loginController", function ($scope, securityService, $rootScope, utils, jsonValue, localStorageService, $location) {
  $scope.login = function() {
    if (!$scope.loginForm.$valid) {
      //$('.errorFromServer').hide();
      $scope.loginError = false;
      return false;
    }
    utils.sendNotification(jsonValue.notifications.loading, $(window).height());
    securityService.login($scope.username, $scope.password);
  }

  $rootScope.$on("$loginSuccess", function () {
    utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
    $scope.loginError = false;
    var protectedPage = localStorageService.get("protectedPage");
    if (protectedPage) {
      localStorageService.remove("protectedPage");
      return $location.url(protectedPage);
    }
    return $location.url("/");
  });

  $rootScope.$on("$loginFailed", function () {
    utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
    $scope.loginError = true;
  });

});