techlooper.controller("salarySharingController", function ($scope, salarySharingService, $rootScope) {
  salarySharingService.init();
  $scope.createReport = function () {
    salarySharingService.validationForm();
  }

  $scope.error = {};
  $scope.jobOfferInfo = {
    skills: []
  };//jobOfferInfo.skills.splice(jobOfferInfo.skills.indexOf(skill),1)

  $scope.removeSkill = function (skill) {
    $scope.jobOfferInfo.skills.splice($scope.jobOfferInfo.skills.indexOf(skill), 1);
    $scope.error = {};
  }

  $scope.addNewSkill = function() {
    var newSkillName = $scope.newSkillName.trim();
    $scope.newSkillName = newSkillName;
    if (newSkillName.length == 0) {
      $scope.error = {};
      return;
    }

    if ($scope.jobOfferInfo.skills.length === 3) {
      var translate = $rootScope.translate;
      $scope.error.newSkillName = translate.maximum3;
      return;
    }
    delete $scope.error.newSkillName;

    var skillLowerCases = $scope.jobOfferInfo.skills.map(function (skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) > 0) {
      var translate = $rootScope.translate;
      $scope.error.existSkillName = translate.hasExist;
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.jobOfferInfo.skills.push(newSkillName);
      $scope.newSkillName = "";
    }
  }
});
