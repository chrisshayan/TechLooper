angular.module('Jobs').controller('searchFormController',
  function (utils, $scope, searchBoxService, jsonValue, animationFactory, $timeout) {

    $timeout(function () {
      utils.sendNotification(jsonValue.notifications.switchScope, $scope);
      searchBoxService.hightlightSKill();
    }, 100);

    //utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.skills = jsonValue.technicalSkill;

    //searchBoxService.hightlightSKill();
    $scope.selectSkill = function (name) {
      var searchText = searchBoxService.getSearchText();
      var skills = searchText.getValue().split(",");
      skills.push(name);
      searchText.setValue(skills);
    }

    animationFactory.animatePage();
  });