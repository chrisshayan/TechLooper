techlooper.controller('landingController', function ($scope, $http, jsonValue, landingService, utils) {
  utils.sendNotification(jsonValue.notifications.switchScope, $scope);

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