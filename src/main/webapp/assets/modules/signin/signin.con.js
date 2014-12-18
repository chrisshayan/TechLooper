angular.module('SignIn').controller('signInController', function ($location, jsonValue, utils, $scope, signInService, shortcutFactory) {
  $scope.authSource = jsonValue.authSource;
  //utils.sendNotification(jsonValue.notifications.switchScope, $scope);
  //utils.sendNotification(jsonValue.notifications.gotData);
  $scope.openOathDialog = signInService.openOathDialog;

  $('.btn-close').click(function () {shortcutFactory.trigger('esc');});
  $('.btn-logo').click(function () {shortcutFactory.trigger('esc');});
});