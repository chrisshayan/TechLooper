angular.module('SignIn').controller('signInController', function ($location, jsonValue, utils, $scope, signInService, navigationService) {
  signInService.initialize();
  $scope.authSource = jsonValue.authSource;
  //utils.sendNotification(jsonValue.notifications.switchScope, $scope);
  //utils.sendNotification(jsonValue.notifications.gotData);
  $scope.openOathDialog = signInService.openOathDialog;

});