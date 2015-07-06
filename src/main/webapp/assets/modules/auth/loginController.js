techlooper.controller("loginController", function ($scope, securityService, $rootScope, utils, jsonValue) {
  $scope.login = function() {
    if (!$scope.loginForm.$valid) {
      $('.errorFromServer').hide();
      return false;
    }
    securityService.login($scope.username, $scope.password);
  }

  $rootScope.$on("$loginSuccess", function () {
    utils.sendNotification(jsonValue.notifications.loading, $(window).height());
    $scope.loginError = false;
  });

  $rootScope.$on("$loginFailed", function () {
    $scope.loginError = true;
    //$('.errorFromServer').show();
  });

});