angular.module('Jobs').controller('searchFormController', function (utils, $scope, searchBoxService, jsonValue, animationFactory, navigationService) {
  //var dataTour = utils.getDataTour();
  //utils.makeTourGuide(dataTour);
  utils.sendNotification(jsonValue.notifications.switchScope, $scope);
  searchBoxService.updateNavi();
  $scope.skills = jsonValue.technicalSkill;

  searchBoxService.hightlightSKill();
  $scope.selectSkill = function (name) {
    var searchText = searchBoxService.getSearchText();
    var skills = searchText.getValue().split(",");
    skills.push(name);
    searchText.setValue(skills);
  }

  animationFactory.animatePage();
  navigationService.addSpaceforNavi();
  navigationService.reSetingPositionLangIcon();
  navigationService.keepNaviBar();
});