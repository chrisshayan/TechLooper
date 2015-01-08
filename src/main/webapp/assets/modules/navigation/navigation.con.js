angular.module("Navigation").controller("navigationController", function ($scope, utils, jsonValue, navigationService, $rootScope) {
  navigationService.initialize();
  navigationService.addSpaceforNavi();
  navigationService.keepNaviBar();
});
