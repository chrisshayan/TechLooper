angular.module('Jobs').controller('searchFormController', function ($scope, searchBoxService, jsonValue, shortcutFactory) {
  searchBoxService.initSearchTextbox($scope);

  $scope.skills = jsonValue.technicalSkill;

  searchBoxService.openSearchForm($(window).height());
  $(window).resize(function () {
    searchBoxService.openSearchForm($(window).height());
  });
  searchBoxService.hightlightSKill();
  $scope.selectSKill = function(name){
  	var searchText = searchBoxService.getSearchText();
  	var listSKill = searchText.getValue().split(",");
  	listSKill.push(name);
  	searchText.setValue(listSKill);
  }
});