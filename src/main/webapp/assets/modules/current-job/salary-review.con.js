techlooper.controller("salaryReviewController", function ($scope, $rootScope, jsonValue, $http, utils, $compile) {
  var jobLevels = $.extend(true, [], jsonValue.jobLevels.filter(function (value) {return value.id > 0;}));
  $scope.$watch("translate", function() {
    if (utils.getView() !== jsonValue.views.salaryReview) {
      return;
    }
    $.each(jobLevels, function (i, jobLevel) {jobLevel.translate = $rootScope.translate[jobLevel.translate];});
  })

  $scope.selectize = {
    locations: {
      items: jsonValue.locations.filter(function (location) {return location.id > 0; }),
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        placeholder: 'Ex: Ho Chi Minh',
        maxItems: 1,
        searchField: ['name']
      }
    },
    jobLevels: {
      items: jobLevels,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        placeholder: 'Ex: Manager',
        maxItems: 1,
        searchField: ['translate']
      }
    },
    industries: {
      items: jsonValue.industriesArray,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        placeholder: 'Ex: IT - Software, IT - Hardware/Networking',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name']
      }
    },
    companySize: {
      items: jsonValue.companySizesArray,
      config: {
        valueField: 'id',
        labelField: 'size',
        delimiter: '|',
        placeholder: 'Ex: 1-49',
        maxItems: 1,
        searchField: ['size']
      }
    }
  }

  $scope.error = {};
  $scope.salaryReview = {
    jobTitle: '',
    skills: [],
    locationId: '',
    jobLevelIds: [],
    jobCategories: [],
    companySizeId: '',
    netSalary: '',
    reportTo: ''
  };

  $scope.removeSkill = function (skill) {
    $scope.salaryReview.skills.splice($scope.salaryReview.skills.indexOf(skill), 1);
    $scope.error = {};
  }

  $scope.addNewSkill = function () {
    if ($scope.newSkillName === undefined) {
      return;
    }
    var newSkillName = $scope.newSkillName.trim();
    $scope.newSkillName = newSkillName;
    if (newSkillName.length == 0) {
      $scope.error = {};
      return;
    }

    if ($scope.salaryReview.skills.length === 3) {
      var translate = $rootScope.translate;
      $scope.error.newSkillName = translate.maximum3;
      return;
    }
    delete $scope.error.newSkillName;

    var skillLowerCases = $scope.salaryReview.skills.map(function (skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) >= 0) {
      var translate = $rootScope.translate;
      $scope.error.existSkillName = translate.hasExist;
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.salaryReview.skills.push(newSkillName);
      $scope.newSkillName = "";
      $("#txtTopSkills").focus();
    }
  }

  $scope.validate = function () {
    var inputs = $(".salary-review-content").find("div:visible").find("[ng-model]");
    var translate = $rootScope.translate;
    $.each(inputs, function (i, input) {
      var modelName = $(input).attr("ng-model");
      if (modelName === 'newSkillName' || $(input).attr("name") === undefined) {
        return true;
      }
      delete $scope.error[modelName];
      var inputValue = $scope.$eval(modelName);
      var notHasValue = ($.type(inputValue) === "array") && inputValue.length > 0;
      notHasValue = notHasValue || ('' + inputValue).length > 0;
      notHasValue || ($scope.error[modelName] = sprintf("%s %s", $(input).attr("name"), translate.isRequired.toLowerCase()));
    });
    return $.isEmptyObject($scope.error);
  }

  $scope.step = "step1";

  $scope.nextStep = function (step) {
    console.log($scope.step);
    if ($scope.step === "step3") {
      return;
    }
    var swstep = step || $scope.step;
    if (!$scope.validate()) {
      return;
    }
    $scope.step = swstep;
    console.log($scope.step);

    switch (swstep) {
      case "step1":
        break;

      case "step2":
        break;

      case "step3":
        var salaryReview = $.extend(true, {}, $scope.salaryReview);
        salaryReview.jobLevelIds = jsonValue.jobLevelsMap['' + salaryReview.jobLevelIds].ids;
        $http.post(jsonValue.httpUri.salaryReview, salaryReview)
          .success(function (data, status, headers, config) {
            console.log(data);
          })
          .error(function (data, status, headers, config) {
          });
        break;
    }

  }

});
