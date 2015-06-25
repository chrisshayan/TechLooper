techlooper.controller("loginController", function ($scope, securityService, $rootScope, $location) {

  $scope.login = function() {
    securityService.login($scope.username, $scope.password);
  }
});