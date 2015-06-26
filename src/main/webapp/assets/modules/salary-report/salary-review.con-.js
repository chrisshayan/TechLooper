techlooper.controller("salaryReviewRelevantController", function ($scope, $rootScope, jsonValue, $http, utils, $translate,
                                                          $route, $location, connectionFactory, $timeout, validatorService) {
  var jobLevels = $.extend(true, [], jsonValue.jobLevels.filter(function (value) {return value.id > 0;}));
  var genders = $.extend(true, [], jsonValue.genders);
  var timeToSends = $.extend(true, [], jsonValue.timeToSends);
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
        placeholder: $translate.instant("exDay"),
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
    $scope.salaryReview.skills || ($scope.salaryReview.skills = []);
    if ($scope.newSkillName === undefined) {
      return;
    }
    var newSkillName = $scope.newSkillName.trim();
    $scope.newSkillName = newSkillName;
    if (newSkillName.length == 0) {
      $scope.error = {};
      return;
    }

    if ($scope.salaryReview.skills.length === 50) {
      var translate = $rootScope.translate;
      $scope.error.newSkillName = translate.maximum50;
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

    if ($scope.salaryReview.jobTitle === '') {
      $scope.jobTitleError = $translate.instant('requiredThisField');
    }
    else {
      delete $scope.jobTitleError;
    }
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
    if (localStorage.getItem('PROMOTION-KEY') === 'yes') {
      $('.partner-company-block').hide();
    }
    if (step === "step2") {
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

            $scope.checkCompanyPromotionRole();
          })
          .error(function (data, status, headers, config) {
          });
        break;
    }
  }

  $scope.createNewReport = function () {
    $route.reload();
  }
  $scope.openFacebookShare = function () {
    window.open(
      'https://www.facebook.com/sharer/sharer.php?u=' + baseUrl + '/renderSalaryReport/' + $translate.use() + '/' + $scope.salaryReview.createdDateTime,
      'name', 'width=450,height=350');
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

    if (campaign.id) {
      $http.get(jsonValue.httpUri.salaryReview + "/" + campaign.id)
        .success(function (data, status, headers, config) {
          $scope.salaryReview = data;
          $scope.salaryReview.campaign = !$.isEmptyObject(campaign);
          //$scope.salaryReport = data.salaryReport;
          utils.sendNotification(jsonValue.notifications.loaded);

          //$scope.checkCompanyPromotionRole();
        });
    }
    else {
      $scope.salaryReview = campaign;
    }
    $scope.step = "step1";
  }
  $scope.errorFeedback = {};
  $scope.validateFeedback = function () {
    if ($scope.survey === undefined) {
      $scope.errorFeedback.understand = $rootScope.translate.requiredThisField;
      $scope.errorFeedback.accurate = $rootScope.translate.requiredThisField;
    }
    else {
      if ($scope.survey.isUnderstandable) {
        delete $scope.errorFeedback.understand;
      }
      else {
        $scope.errorFeedback.understand = $rootScope.translate.requiredThisField;
      }
      if ($scope.survey.isAccurate) {
        delete $scope.errorFeedback.accurate;
      }
      else {
        $scope.errorFeedback.accurate = $rootScope.translate.requiredThisField;
      }

    }
    return $.isEmptyObject($scope.errorFeedback);
  }
  $scope.submitSurvey = function () {
    if ($scope.validateFeedback() == true) {
      $scope.survey.salaryReviewId = $scope.salaryReview.createdDateTime;
      $http.post("saveSalaryReviewSurvey", $scope.survey)
        .success(function (data, status, headers, config) {
          $scope.survey.submitted = true;
        })
        .error(function (data, status, headers, config) {
        });
      $('.email-get-similar-jobs').focus();
    }
    else {
      return;
    }
  }

  $scope.removeBoxContent = function (cls) {
    //$scope.survey = {closed: true};
    $('.' + cls).slideUp("normal", function () { $(this).remove(); });
  }

  $scope.doJobAlert = function () {
    $('.email-me-similar-jobs').hide();
    $('.email-similar-jobs-block').slideDown("normal");
    var jobAlert = $.extend({}, $scope.salaryReview);
    jobAlert.frequency = timeToSends[0].id;
    delete jobAlert.salaryReport;
    delete jobAlert.topPaidJobs;
    $scope.jobAlert = jobAlert;
  }
  $scope.promotionCompanyInfo = {
    "title": jsonValue.companyPromotion.title,
    "tagLine": jsonValue.companyPromotion.tagLine,
    "images": jsonValue.companyPromotion.images
  };
  $scope.sendCitibankPromotion = function () {
    var formContent = $('.partner-company-form');
    if (!formContent.hasClass('active')) {
      formContent.slideDown("normal");
      formContent.addClass('active');
      $('.note-Partner-Company-Form').show();
      $('.apply-now-block').find('button').addClass('disabled');
    }
    else {
      var error = validatorService.validate($(".partner-company-form").find('input'));
      $scope.error = error;
      if (!$.isEmptyObject(error)) {
        return;
      }
      else {
        $scope.promotion.salaryReviewId = $scope.salaryReview.createdDateTime;
        $('.partner-company-detail').find('h4').hide();
        formContent.hide();
        $('.apply-now-block').hide();
        $('.note-Partner-Company-Form').hide();
        if ($scope.promotion.paymentMethod == 'BANK_TRANSFER') {
          $http.post("promotion/citibank/creditCard", $scope.promotion)
            .success(function () {
              $('.transfer').slideDown("normal");
            }).error(function (data, status, headers, config) {
              $('.cash').slideDown("normal");
            });
        }
        else {
          $('.cash').slideDown("normal");
        }
        $scope.citiBankSubmit = true;
        localStorage.setItem('PROMOTION-KEY', 'yes');
      }
    }
  }
  $scope.citiBankSubmit = false;
  $scope.promotionRule = false;
  $scope.checkCompanyPromotionRole = function () {
    if (($scope.salaryReview.usdToVndRate * $scope.salaryReview.netSalary) >= jsonValue.companyPromotion.minSalary && jsonValue.companyPromotion.AcceptedCity.indexOf($scope.salaryReview.locationId) >= 0) {
      $scope.promotionRule = true;
    }
    return $scope.promotionRule;
  }
  $scope.checkSelect = function () {
    var flg = $('#iAgree').hasClass("ng-invalid-required");
    if (flg) {
      $('.apply-now-block').find('button').addClass('disabled');
    }
    else {
      $('.apply-now-block').find('button').removeClass('disabled');
    }
  }
  var jobAlertTimeout;
  $scope.$watch("jobAlert", function () {
    if (jobAlertTimeout !== undefined) {
      $timeout.cancel(jobAlertTimeout);
    }
    if (!$scope.jobAlert) {
      return;
    }

    jobAlertTimeout = $timeout(function () {
      var jobAlert = $.extend({}, $scope.jobAlert);
      jobAlert.jobLevelIds = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].ids;
      connectionFactory.searchJobAlert(jobAlert).finally(null, function (data) {
        $scope.jobsTotal = data.total;
      });
      jobAlertTimeout = undefined;
    }, 500);
  }, true);

  $scope.createJobAlert = function () {
    var error = validatorService.validate($(".email-similar-jobs-block").find("[tl-model]"));
    $scope.error = error;
    if (!$.isEmptyObject(error)) {
      return;
    }
    var jobAlert = $.extend({}, $scope.jobAlert);
    jobAlert.jobLevel = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].alertId;
    jobAlert.lang = jsonValue.languages['' + $translate.use()];
    connectionFactory.createJobAlert(jobAlert).then(function () {
      $('.email-similar-jobs-block').slideUp("normal", function () {
        $('.success-alert-box').addClass('show');
        $timeout(function () {
          $('.success-alert-box').removeClass('show');
        }, 2000);
      });
    });
  }

  $scope.sendMeNow = function () {
    var error = validatorService.validate($(".send-me-form").find('input'));
    $scope.error = error;
    if (!$.isEmptyObject(error)) {
      return;
    }

    $scope.sendMeReport.salaryReviewId = $scope.salaryReview.createdDateTime;
    $scope.sendMeReport.lang = $translate.use();
    $http.post("salaryReview/placeSalaryReviewReport", $scope.sendMeReport)
      .success(function () {
        $('.thanks-message-for-send-me-success').addClass('show');
      })
      .error(function () {
        $('.thanks-message-for-send-me-fail').addClass('show');
      });
    $('.send-me-form').hide();
  }

  var timeoutFn;
  $scope.jobTitles = [];
  $scope.$watch("salaryReview.jobTitle", function () {
    if (timeoutFn !== undefined) {
      $timeout.cancel(timeoutFn);
    }
    if (!$scope.salaryReview.jobTitle) {
      $scope.jobTitles.length = 0;
      return;
    }

    timeoutFn = $timeout(function () {
      $.get("suggestion/jobTitle/" + $scope.salaryReview.jobTitle)
        .success(function (data) {
          $scope.jobTitles = data.items.map(function (item) {return item.name;});
          $scope.$apply();
        });
      timeoutFn = undefined;
    }, 0);
  }, true);

  $scope.$watch("salaryReview.reportTo", function () {
    if (timeoutFn !== undefined) {
      $timeout.cancel(timeoutFn);
    }
    if (!$scope.salaryReview.reportTo) {
      $scope.jobTitles = [];
      return;
    }

    timeoutFn = $timeout(function () {
      $.get("suggestion/jobTitle/" + $scope.salaryReview.reportTo)
        .success(function (data) {
          $scope.jobTitles = data.items.map(function (item) {return item.name;});
          $scope.$apply();
        });
      timeoutFn = undefined;
    }, 0);
  }, true);

  $scope.$watch("newSkillName", function () {
    if (timeoutFn !== undefined) {
      $timeout.cancel(timeoutFn);
    }
    $scope.skills = [];
    if (!$scope.newSkillName) {
      return;
    }

    timeoutFn = $timeout(function () {
      $.get("suggestion/skill/" + $scope.newSkillName)
        .success(function (data) {
          $scope.skills = data.items.map(function (item) {return item.name;});
          $scope.$apply();
        });
      timeoutFn = undefined;
    }, 0);
  }, true);

  var windScroll = $(window).scrollTop();
  if ($('.salary-review-block').position().top <= windScroll) {
    $('.navi-step-salary-review').addClass('fixed');
  }
  $(window).scroll(function() {
    windScroll = $(window).scrollTop();
    if (windScroll > 0) {
      if ($('.salary-review-block').position().top <= windScroll) {
        $('.navi-step-salary-review').addClass('fixed');
      } else {
        $('.navi-step-salary-review').removeClass('fixed');
      }
    }
  });
});
