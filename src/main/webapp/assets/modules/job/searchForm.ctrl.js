angular.module('Jobs').controller('searchFormController', function ($scope, searchBoxService, jsonValue) {
  searchBoxService.initializeIntelligent($scope);
  $scope.skills = jsonValue.technicalSkill;
});