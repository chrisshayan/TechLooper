techlooper.controller("salaryReviewController", function ($scope, $rootScope, jsonValue, $http, utils, $translate,
                                                          $route, $location, $anchorScroll) {
  var jobLevels = $.extend(true, [], jsonValue.jobLevels.filter(function (value) {return value.id > 0;}));
  var genders = $.extend(true, [], jsonValue.genders.filter(function (value) {return value.id > 0;}));
  var timeToSends = $.extend(true, [], jsonValue.timeToSends.filter(function (value) {return value.id > 0;}));
  var campaign = $location.search();
  $scope.$watch("translate", function () {
    if (utils.getView() !== jsonValue.views.salaryReview || $rootScope.translate === undefined) {
      return;
    }
    var translate = $rootScope.translate;
    $.each(jobLevels, function (i, jobLevel) {jobLevel.translate = translate[jobLevel.translate];});
    $.each(genders, function (i, item) {item.translate = translate[item.translate];});
    $.each(timeToSends, function (i, item) {item.translate = translate[item.translate];});

    $.each([
      {item: "genders", translate: "exMale"},
      {item: "locations", translate: "exHoChiMinh"},
      {item: "jobLevels", translate: "exManager"},
      {item: "industries", translate: "exItSoftware"},
      {item: "companySize", translate: "ex149"},
      {item: "timeToSends", translate: "exDay"}
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
    genders: {
      items: genders,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate'],
        placeholder: $translate.instant("exMale"),
        onInitialize: function (selectize) {
          $scope.selectize.genders.$elem = selectize;
        }
      }
    },
    yobs: {
      items: jsonValue.yobs,
      config: {
        valueField: 'value',
        labelField: 'value',
        delimiter: '|',
        searchField: ['value'],
        maxItems: 1,
        placeholder: $translate.instant("exYob")
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
    timeToSends: {
      items: timeToSends,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate'],
        onInitialize: function (selectize) {
          $scope.selectize.timeToSends.$elem = selectize;
        }
      }
    }
  }

  $scope.error = {};
  $scope.salaryReview = {
    genderId: '',
    jobTitle: '',
    skills: [],
    locationId: '',
    jobLevelIds: [],
    jobCategories: [],
    companySizeId: '',
    netSalary: '',
    reportTo: '',
    timeToSendId: ''
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
    $scope.salaryReview.skills.length || ($scope.error.skills = $rootScope.translate.requiredThisField);
    $scope.salaryReview.skills.length && (delete $scope.error.skills);

    var error = $.extend(true, {}, $scope.error);
    delete error.existSkillName;
    delete error.newSkillName;
    return $.isEmptyObject(error);
  }

  $scope.step = "step1";

  $scope.nextStep = function (step, priorStep) {
    if ((($scope.step === priorStep || step === "step3") && !$scope.validate()) || $scope.step === "step3") {
      return;
    }

    if(step === "step2"){
      $('.locationSelectbox input').click();
    }
    var swstep = step || $scope.step;
    $scope.step = swstep;
    $scope.error = {};

    switch (swstep) {
      case "step3":
        var salaryReview = $.extend(true, {}, $scope.salaryReview);
        salaryReview.jobLevelIds = jsonValue.jobLevelsMap['' + salaryReview.jobLevelIds].ids;
        utils.sendNotification(jsonValue.notifications.switchScope);

        $http.post(jsonValue.httpUri.salaryReview, salaryReview)
          .success(function (data, status, headers, config) {
            $scope.salaryReview = data;
            $scope.salaryReview.campaign = !$.isEmptyObject(campaign);
            $scope.salaryReport = data.salaryReport;
            utils.sendNotification(jsonValue.notifications.loaded);
          })
          .error(function (data, status, headers, config) {
          });
        break;
    }
  }

  $scope.createNewReport = function () {
    $route.reload();
  }

  $scope.openFacebookShare = function() {
    window.open(
      'https://www.facebook.com/sharer/sharer.php?u=' + baseUrl + '/renderSalaryReport/' + $translate.use() + '/' + $scope.salaryReview.createdDateTime,
      'name','width=450,height=350');
  }

  /*
   {
     "jobTitle": "java developer",
     "skills": ["java", "j2ee", "spring framework"],
     "locationId": "29",
     "jobLevelIds": [5, 6],
     "jobCategories": ["35"],
     "companySizeId": "",
     "netSalary": 1000,
     "reportTo": "manager",
   }
   http://localhost:8080/#/salary-review?campaign=vnw&lang=vi&jobTitle=java&skills=["swing","hibernate"]&locationId="29"&jobLevelIds=[5, 6]&jobCategories=["35"]&companySizeId=""&netSalary=1000
   * */
  if (!$.isEmptyObject(campaign)) {
    for (var prop in campaign) {
      try {
        campaign[prop] = JSON.parse(campaign[prop]);
      }
      catch (e) {}
    }
    $scope.salaryReview = campaign;
    //$scope.salaryReview.campaign = true;
    $scope.step = "step1";
  }

  $scope.submitSurvey = function() {
    $scope.survey.salaryReviewId = $scope.salaryReview.createdDateTime;
    $http.post("saveSurvey", $scope.survey)
      .success(function (data, status, headers, config) {
        $scope.survey.submitted = true;
      })
      .error(function (data, status, headers, config) {
      });
  }

  $scope.removeBoxContent = function(cls){
    $scope.survey = {closed: true};
    $('.'+cls).slideUp("normal", function() { $(this).remove(); } );
  }

  //$anchorScroll();
});
