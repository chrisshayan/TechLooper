angular.module('Common').controller('translationController', function ($scope, translationService, utils, $translate, jsonValue) {
  translationService.initialize($scope);
  $scope.icoLanguage = translationService.getNextLanguage();
});
