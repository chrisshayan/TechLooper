angular.module("Navigation").controller("navigationController", function ($scope, navigationService) {
  navigationService.initialize();
});
