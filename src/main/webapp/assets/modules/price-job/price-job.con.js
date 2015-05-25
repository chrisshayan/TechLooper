techlooper.controller("priceJobController", function ($scope, $rootScope, jsonValue, $http, utils, $translate, $route, $location) {

  var jobLevels = $.extend(true, [], jsonValue.jobLevels.filter(function (value) {return value.id > 0;}));

  $scope.$watch("translate", function () {
    if ($rootScope.translate === undefined) {
      return;
    }

    var translate = $rootScope.translate;
    $.each(jobLevels, function (i, jobLevel) {jobLevel.translate = translate[jobLevel.translate];});

    $.each([
      {item: "locations", translate: "exHoChiMinh"},
      {item: "jobLevels", translate: "exManager"},
      {item: "industries", translate: "exItSoftware"},
      {item: "companySize", translate: "ex149"}
    ], function (i, select) {
      if (!$scope.selectize[select.item].$elem) {
        return true;
      }
      $scope.selectize[select.item].$elem.settings.placeholder = translate[select.translate];
      $scope.selectize[select.item].$elem.updatePlaceholder();
    });
  });

  $scope.selectize = {
    locations: {
      items: jsonValue.locations.filter(function (location) {return location.id > 0; }),
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 1,
        searchField: ['name'],
        placeholder: $translate.instant("exHoChiMinh"),
        onInitialize: function (selectize) {
          $scope.selectize.locations.$elem = selectize;
        }
      }
    },
    jobLevels: {
      items: jobLevels,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate'],
        placeholder: $translate.instant("exManager"),
        onInitialize: function (selectize) {
          $scope.selectize.jobLevels.$elem = selectize;
        }
      }
    },
    industries: {
      items: jsonValue.industriesArray,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name'],
        placeholder: $translate.instant("exItSoftware"),
        onInitialize: function (selectize) {
          $scope.selectize.industries.$elem = selectize;
        }
      }
    },
    companySize: {
      items: jsonValue.companySizesArray,
      config: {
        valueField: 'id',
        labelField: 'size',
        delimiter: '|',
        maxItems: 1,
        searchField: ['size'],
        placeholder: $translate.instant("ex149"),
        onInitialize: function (selectize) {
          $scope.selectize.companySize.$elem = selectize;
        }
      }
    },
    languages: {
      items: jsonValue.languagesJob,
      config: {
        valueField: 'name',
        labelField: 'name',
        delimiter: '|',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name'],
        placeholder: $translate.instant("exLanguages"),
        onInitialize: function (selectize) {
          $scope.selectize.languages.$elem = selectize;
        }
      }
    },
    education: {
      items: jsonValue.educationLevel,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 1,
        searchField: ['name'],
        placeholder: $translate.instant("exEducation"),
        onInitialize: function (selectize) {
          $scope.selectize.education.$elem = selectize;
        }
      }
    },
    experience: {
      items: jsonValue.yearsOfExperience,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 1,
        searchField: ['name'],
        placeholder: $translate.instant("exExperience"),
        onInitialize: function (selectize) {
          $scope.selectize.education.$elem = selectize;
        }
      }
    }
  }

  $scope.selectedTime = $translate.instant("day");
  $scope.error = {};
  $scope.priceJob = {
    jobCategories: [],
    companySizeId: '',
    locationId: '',
    jobTitle: '',
    jobLevelIds: [],
    skills: [],
    languagesId: []
  };

  $scope.removeSkill = function (skill) {
    $scope.priceJob.skills.splice($scope.priceJob.skills.indexOf(skill), 1);
    $scope.error = {};
  }

  $scope.addNewSkill = function () {
    $scope.priceJob.skills || ($scope.priceJob.skills = []);
    if ($scope.newSkillName === undefined) {
      return;
    }
    var newSkillName = $scope.newSkillName.trim();
    $scope.newSkillName = newSkillName;
    if (newSkillName.length == 0) {
      $scope.error = {};
      return;
    }

    if ($scope.priceJob.skills.length === 3) {
      var translate = $rootScope.translate;
      $scope.error.newSkillName = translate.maximum3;
      return;
    }
    delete $scope.error.newSkillName;

    var skillLowerCases = $scope.priceJob.skills.map(function (skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) >= 0) {
      var translate = $rootScope.translate;
      $scope.error.existSkillName = translate.hasExist;
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.priceJob.skills.push(newSkillName);
      $scope.newSkillName = "";
      $("#txtTopSkills").focus();
    }
  }

  $scope.step = "step1";
  $scope.validate = function(){
    var inputs = $(".price-job-step-content").find("div:visible").find("[ng-model]");
    $.each(inputs, function (i, input) {
      var modelName = $(input).attr("ng-model");
      if (modelName === 'newSkillName' || $(input).attr("name") === undefined) {
        return true;
      }
      delete $scope.error[modelName];
      var inputValue = $scope.$eval(modelName);
      var notHasValue = ($.type(inputValue) === "array") && (inputValue.length === 0);
      notHasValue = notHasValue || !inputValue;
      notHasValue = notHasValue || (inputValue.length <= 0);
      notHasValue && ($scope.error[modelName] = $rootScope.translate.requiredThisField);
    });
    if($scope.step == "step2"){
      $scope.priceJob.skills.length || ($scope.error.skills = $rootScope.translate.requiredThisField);
      $scope.priceJob.skills.length && (delete $scope.error.skills);
    }
    var error = $.extend(true, {}, $scope.error);
    delete error.existSkillName;
    delete error.newSkillName;
    return $.isEmptyObject(error);
  }

  $scope.nextStep = function (step, priorStep) {
    if ((($scope.step === priorStep || step === "step3") && !$scope.validate()) || $scope.step === "step3") {
      return;
    }
    var swstep = step || $scope.step;
    $scope.step = swstep;

    switch (swstep) {
      case "step3":
        var priceJob = $.extend(true, {}, $scope.priceJob);
        priceJob.jobLevelIds = jsonValue.jobLevelsMap[priceJob.jobLevelIds].ids;
        utils.sendNotification(jsonValue.notifications.switchScope);
        $http.post("priceJob", priceJob)
          .success(function (data, status, headers, config) {
            $scope.priceJob = data;
            $scope.priceJob.jobCategoryLabels = $scope.priceJob.jobCategories.map(function(cat) {
              return jsonValue.industries[cat].value;
            });
            utils.sendNotification(jsonValue.notifications.loaded);
          });
        break;
    }
  }
  $scope.createNewReport = function () {
    $route.reload();
  }
});