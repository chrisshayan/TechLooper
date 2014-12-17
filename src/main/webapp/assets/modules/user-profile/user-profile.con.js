angular.module('UserProfile').controller('userProfileController', function ($scope, jsonValue, userProfileFactory) {
  $scope.accounts = jsonValue.accountSignin;
  userProfileFactory.customScrollBar();
  userProfileFactory.resizeScreen();
  userProfileFactory.collapseContent();
  userProfileFactory.naviControl();
  userProfileFactory.subNaviControl();
});