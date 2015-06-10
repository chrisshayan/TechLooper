techlooper.controller("salaryReviewController", function ($location, $scope, vnwConfigService, $http, jsonValue,
                                                          utils, $route, validatorService, $translate, $window) {
  var state = {
    init: true,

    default: {
      showJob: true,
      skillBoxConfig: {
        placeholder: "mostRelevantSkills.placeholder",
        items: [],
        required: true
      },
      order: 1,

      tabs: [
        {title: "aboutYourJob", class: "active showNavi", onClick: function(tab) {$scope.changeState(state.default);}},
        {title: "aboutYourCompany", onClick: function(tab) {$scope.changeState(state.company);}},
        {title: "yourSalaryReport", class: "noPointer", onClick: function(tab) {$scope.changeState(state.report);}}
      ],

      rootClass: "jobRoot"
    },

    company: {
      showCompany: true,
      order: 2,

      tabs: [
        {title: "aboutYourJob", class: "active", onClick: function(tab) {$scope.changeState(state.default);}},
        {title: "aboutYourCompany", class: "active showNavi", onClick: function(tab) {$scope.changeState(state.company);}},
        {title: "yourSalaryReport", onClick: function(tab) {$scope.changeState(state.report);}}
      ],

      rootClass: "companyRoot"
    },

    report: {
      showReport: true,
      ableCreateNewReport: true,
      rootClass: "user-personal-info",
      editableSalaryReview: true,
      showSendReport: true,
      showJobAlertButton: true,
      order: 3,

      tabs: [
        {title: "aboutYourJob", class: "active", onClick: function(tab) {$scope.changeState(state.default);}},
        {title: "aboutYourCompany", class: "active", onClick: function(tab) {$scope.changeState(state.company);}},
        {title: "yourSalaryReport", class: "active showNavi", onClick: function(tab) {$scope.changeState(state.report);}}
      ]
    }
  }

  state.orders = [state.default, state.company, state.report];

  var campaign = $location.search();

  $scope.selectize = vnwConfigService;
  $scope.salaryReview = {}

  $scope.validateSalaryReview = function (st) {
    if (!$scope.salaryReview) {
      return true;
    }

    var elems = $("." + st.rootClass).find("[validate]");
    var error = validatorService.validate(elems);

    $scope.salaryReview.skills = $scope.salaryReview.skills || [];
    if ($scope.salaryReview.skills.length === 0) {
      error.skills = $translate.instant('requiredThisField');
    }
    $scope.error = error;


    if (!$.isEmptyObject(error)) {
      return false;
    }

    return true;
  }

  $scope.changeState = function (st, validateCurrentState) {
    if(st === 'company'){
      var val = $('#txtBaseSalary').val();
      if(val ===''){
        $scope.salaryNetError = $translate.instant('requiredThisField');
      }else{
        delete $scope.salaryNetError;
      }
    }
    var bodyHeight = $(window).height();
    if ($('body').height() <= bodyHeight) {
      $('.navi-step-salary-review').removeClass('fixed');
    }
    
    st = st || state.default;
    var preferState = $.extend(true, {}, (typeof st === 'string') ? state[st] : st);
    var valid = true;
    if (!state.init) {
      $.each(state.orders, function(i, stateItem) {
        if (stateItem.order > preferState.order) {
          return false;
        }
        if (stateItem.order === preferState.order && !validateCurrentState) {
          return false;
        }

        if (!$scope.validateSalaryReview(stateItem)) {
          valid = false;
          //if ($scope.state.validateAllState) {
          //  return true;
          //}
          preferState = $.extend(true, {}, stateItem);
          return false;
        }
      });
    }
    delete state.init;

    if (!valid) {
      return false;
    }

    $scope.state = preferState;
    $scope.$broadcast("state change success");
    return true;
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

  var preferState = state.default;
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
          //$scope.salaryReview.hasCam = !$.isEmptyObject(campaign);
          utils.sendNotification(jsonValue.notifications.loaded);
        });
    }
    else {
      $scope.salaryReview = campaign;
    }
  }

  $scope.changeState(preferState);

  $scope.checkSelect = function () {
    var flg = $('#iAgree').hasClass("ng-invalid-required");
    if (flg) {
      $('.apply-now-block').find('button').addClass('disabled');
    }
    else {
      $('.apply-now-block').find('button').removeClass('disabled');
    }
  }

  $scope.$watch("salaryReport", function (salaryReport) {
    if (!salaryReport) {return;}

    if ($.type(salaryReport.percentRank) === "number") {
      //$scope.state.showSendReport = true;
      $scope.state.showSurvey = true;
      //$scope.changeState(state.reportSurvey);
    }
  });

  $scope.reload = function() {
    $route.reload();
  }

  localStorage.setItem('PROMOTION-KEY', 'no');

  //$scope.$watch("salaryReview", function (newVal, oldVal) {
  //  for (var prop in newVal) {
  //    if (newVal[prop] !== oldVal[prop]) {
  //      validatorService.validateElem($("[ng-model='salaryReview." + prop + "']"), newVal[prop], $scope.error);
  //    }
  //  }
  //}, true);
  //
  //$scope.error = {};
  //
  //$scope.$watch("salaryReview.skills", function (newVal, oldVal) {
  //  //validatorService.validateElem($("[ng-model='salaryReview." + prop + "']"), newVal[prop], $scope.error);
  //  if (!newVal) return;
  //  //newVal = newVals || [];
  //  if (newVal.length === 0) {
  //    $scope.error.skills = $translate.instant('requiredThisField');
  //  }
  //  else {
  //    delete $scope.error.skills;
  //  }
  //
  //}, true);
});
