angular.module('Jobs').controller('searchFormController', function ($scope, searchBoxService, jsonValue) {
  searchBoxService.initSearchTextbox($scope);

  $scope.skills = jsonValue.technicalSkill;

  searchBoxService.openSearchForm($(window).height());
  $(window).resize(function () {
    searchBoxService.openSearchForm($(window).height());
  });

});