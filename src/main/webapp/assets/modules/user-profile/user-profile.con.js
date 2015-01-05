angular.module('UserProfile').controller('userProfileController', function ($scope, jsonValue, shortcutFactory, userProfileFactory) {
  $scope.accounts = jsonValue.authSource;
  userProfileFactory.customScrollBar();
  userProfileFactory.resizeScreen();
  userProfileFactory.collapseContent();
  $('.btn-close').click(function () {shortcutFactory.trigger('esc');});
  $('.btn-logo').click(function () {shortcutFactory.trigger('esc');});
});