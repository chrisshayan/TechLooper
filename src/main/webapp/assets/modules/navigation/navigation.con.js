angular.module("Navigation").controller("navigationController", function ($scope, securityService) {

  $scope.logout = function() {
    securityService.logout();
  }
});
