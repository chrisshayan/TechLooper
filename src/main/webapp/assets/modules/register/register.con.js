angular.module('Register').controller('registerController',
  function ($scope, connectionFactory, jsonValue, localStorageService, utils, registerService, userService, navigationService) {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.authSource = jsonValue.authSource;
    $scope.openOathDialog = registerService.openOathDialog;
    $scope.salaryOptions = registerService.getSalaryOptions();

    userService.getUserInfo()
      .then(function () {
        localStorageService.remove(jsonValue.storage.back2Me, "true");
        utils.sendNotification(jsonValue.notifications.gotData);
      })
      .catch(function () {
        localStorageService.set(jsonValue.storage.back2Me, "true");
        utils.sendNotification(jsonValue.notifications.loginFailed);
      });
    navigationService.addSpaceforNavi();
    navigationService.reSetingPositionLangIcon();
    navigationService.keepNaviBar();
  });