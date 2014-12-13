angular.module('Register').controller('registerController',
  function ($scope, connectionFactory, jsonValue, localStorageService, utils, registerService) {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.authSource = jsonValue.authSource;
    $scope.$on(jsonValue.events.userInfoEmail, function (event, userInfo) {
      registerService.updateUserInfo(userInfo);
      utils.sendNotification(jsonValue.notifications.gotData);
    });
    var key = localStorageService.get(jsonValue.storage.key);
    key !== undefined && connectionFactory.findUserInfoByEmail({key: key});
  });