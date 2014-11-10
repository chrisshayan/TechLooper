angular.module('Jobs').controller('searchFormController', function ($scope, searchBoxService, jsonValue, animationFactory) {
  // TODO use observer later
  //searchBoxService.initSearchTextbox($scope);
  utils.sendNotification(jsonValue.notifications.switchScope, $scope);
  $scope.skills = jsonValue.technicalSkill;

  searchBoxService.hightlightSKill();
  $scope.selectSkill = function (name) {
    var searchText = searchBoxService.getSearchText();
    var skills = searchText.getValue().split(",");
    skills.push(name);
    searchText.setValue(skills);
  }

  animationFactory.animatePage();
});