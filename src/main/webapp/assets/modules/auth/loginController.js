techlooper.controller("loginController", function ($scope, securityService, $rootScope, utils, jsonValue, localStorageService, $location) {
  $scope.login = function() {
    if (!$scope.loginForm.$valid) {
      $scope.loginError = false;
      return false;
    }
    utils.sendNotification(jsonValue.notifications.loading, $(window).height());

    securityService.login($scope.username, $scope.password)
      .success(function() {
        //utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
        //$scope.loginError = false;
        //securityService.getCurrentUser().success(function() {
        //  securityService.routeByRole();
        //});
      })
      .error(function() {
        utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
        $scope.loginError = true;
      });
  }

  //securityService.routeByRole();
});