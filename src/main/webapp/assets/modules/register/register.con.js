angular.module('Register').controller('registerController',
  function ($scope, connectionFactory, jsonValue, localStorageService, utils, registerService) {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.authSource = jsonValue.authSource;
    $scope.openOathDialog = registerService.openOathDialog;
    $scope.salaryOptions = registerService.getSalaryOptions();
    $scope.$on(jsonValue.events.userInfo, function (event, userInfo) {
      $scope.userInfo = userInfo;
      registerService.updateUserInfo(userInfo);
      utils.sendNotification(jsonValue.notifications.gotData);
      utils.apply();
    });
    connectionFactory.findUserInfoByKey();
  });