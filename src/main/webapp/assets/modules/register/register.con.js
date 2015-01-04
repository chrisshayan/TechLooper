angular.module('Register').controller('registerController',
  function ($scope, connectionFactory, jsonValue, localStorageService, utils, registerService, $rootScope) {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.authSource = jsonValue.authSource;
    $scope.openOathDialog = registerService.openOathDialog;
    $scope.salaryOptions = registerService.getSalaryOptions();

    var updateUserInfo = function() {
      registerService.updateUserInfo($rootScope.userInfo);
      utils.sendNotification(jsonValue.notifications.gotData);
      //utils.apply();
    }

    $scope.$on(jsonValue.events.userInfo, function (event, userInfo) {
      updateUserInfo();
    });

    if ($rootScope.userInfo === undefined) {
      connectionFactory.findUserInfoByKey();
    }
  });