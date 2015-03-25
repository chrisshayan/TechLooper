techlooper.controller('landingController', function ($scope, $http, jsonValue, landingService) {
  $scope.validationRegister = function () {
    landingService.validateForm();
  };
  $('html').keypress(function (e) {
    if (e.which == 13) {
      landingService.validateForm();
    }
  });
  landingService.init();
});