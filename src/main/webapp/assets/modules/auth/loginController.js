techlooper.controller("loginController", function ($scope, securityService, $rootScope, $location) {
  $scope.login = function() {
    if (!$scope.loginForm.$valid) {
      $('.errorFromServer').hide();
      return false;
    }
    securityService.login($scope.username, $scope.password);
  }
});