angular.module('Jobs').controller('searchFormController', ["$scope", "searchBoxService", "jsonValue",
  function ($scope, searchBoxService, jsonValue) {
  searchBoxService.initializeIntelligent($scope);
  $scope.skills = jsonValue.technicalSkill;
}]);