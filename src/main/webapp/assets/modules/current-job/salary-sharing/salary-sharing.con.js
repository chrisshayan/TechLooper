techlooper.controller("salarySharingController", function ($scope, salarySharingService, $rootScope, jsonValue) {
  salarySharingService.init();
  $scope.createReport = function () {
    salarySharingService.validationForm();
  }

  var industriesJson = jsonValue.industriesArray;
  var companiesJson = jsonValue.companySizesArray;

  $scope.selectize = {
    locations: {
      items: jsonValue.locations,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        placeholder: 'Ex: Ho Chi Minh',
        maxItems: 1
      }
    },
    jobLevels: {
      items: jsonValue.jobLevels,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        placeholder: 'Ex: Manager',
        maxItems: 1
      }
    },
    industries: {
      items: industriesJson,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        placeholder: 'Ex: IT - Software, IT - Hardware/Networking',
        maxItems: 3
      }
    },
    companySize: {
      items: companiesJson,
      config: {
        valueField: 'id',
        labelField: 'size',
        delimiter: '|',
        placeholder: 'Ex: 1-49',
        maxItems: 1
      }
    }
  };
  //$scope.jobLevels = jsonValue.jobLevels;

  $scope.error = {};
  $scope.jobOfferInfo = {
    skills: [],
    locationId: '',
    jobLevelId: '',
    industryId: '',
    companySizeId: ''
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
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) >= 0) {
      var translate = $rootScope.translate;
      $scope.error.existSkillName = translate.hasExist;
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.jobOfferInfo.skills.push(newSkillName);
      $scope.newSkillName = "";
      $("#txtTopSkills").focus();
    }
  }
});
