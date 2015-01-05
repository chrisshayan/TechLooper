angular.module('Common').controller('translationController', function ($scope, translationService) {
  translationService.initialize($scope);
  $scope.icoLanguage = translationService.getNextLanguage();
});
