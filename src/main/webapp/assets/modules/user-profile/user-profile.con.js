angular.module('UserProfile').controller('userProfileController', function ($scope, jsonValue) {
  $scope.accounts = jsonValue.authSource;
  userProfileFactory.customScrollBar();
  userProfileFactory.resizeScreen();
  userProfileFactory.collapseContent();
});