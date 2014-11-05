angular.module('Jobs').controller('searchFormController', function ($scope, searchBoxService, jsonValue, animationFactory) {
  searchBoxService.initSearchTextbox($scope);

  $scope.skills = jsonValue.technicalSkill;

  searchBoxService.hightlightSKill();
  $scope.selectSKill = function (name) {
    var searchText = searchBoxService.getSearchText();
    var skills = searchText.getValue().split(",");
    skills.push(name);
    searchText.setValue(skills);
  }

  animationFactory.animatePage();
});