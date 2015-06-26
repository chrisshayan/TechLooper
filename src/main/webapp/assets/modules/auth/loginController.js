techlooper.controller("loginController", function ($scope, securityService, $rootScope, $location) {
  $scope.login = function() {
    if (!$scope.loginForm.$valid) {
      return false;
    }
    securityService.login($scope.username, $scope.password);
  }
});