angular.module('Register').controller('registerController',
  function ($scope, connectionFactory, jsonValue, localStorageService, utils, registerService) {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.authSource = jsonValue.authSource;
    //$scope.$on(jsonValue.events.userInfo, function (event, userInfo) {
    //  registerService.updateUserInfo(userInfo);
    //  utils.sendNotification(jsonValue.notifications.gotData);
    //});
    //var key = localStorageService.get(jsonValue.storage.key);
    connectionFactory.findUserInfoByKey({key: localStorageService.get(jsonValue.storage.key)})
      .success(function (userInfo) {
        registerService.updateUserInfo(userInfo);
        utils.sendNotification(jsonValue.notifications.gotData);
      });
  });