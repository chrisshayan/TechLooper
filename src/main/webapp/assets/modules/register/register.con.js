angular.module('Register').controller('registerController',
  function ($scope, connectionFactory, jsonValue, localStorageService, utils, registerService, userService, navigationService) {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.authSource = jsonValue.authSource;
    $scope.openOathDialog = registerService.openOathDialog;
    $scope.salaryOptions = registerService.getSalaryOptions();

    userService.getUserInfo().then(function () {
      utils.sendNotification(jsonValue.notifications.gotData);
    });
    navigationService.addSpaceforNavi();
    navigationService.reSetingPositionLangIcon();
    navigationService.keepNaviBar();
  });