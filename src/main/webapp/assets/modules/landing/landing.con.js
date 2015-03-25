techlooper.controller('landingController', function ($scope, $http, jsonValue, landingService) {
  $scope.validationRegister = function () {
    landingService.validateForm();
  };
  landingService.init();
});