angular.module('UserProfile').controller('userProfileController', function ($scope, jsonValue, shortcutFactory, userProfileFactory) {
  $scope.accounts = jsonValue.accountSignin;
  userProfileFactory.customScrollBar();
  userProfileFactory.resizeScreen();
  userProfileFactory.collapseContent();
  userProfileFactory.naviControl();
  userProfileFactory.subNaviControl();
  $('.btn-close').click(function () {shortcutFactory.trigger('esc');});
  $('.btn-logo').click(function () {shortcutFactory.trigger('esc');});
});