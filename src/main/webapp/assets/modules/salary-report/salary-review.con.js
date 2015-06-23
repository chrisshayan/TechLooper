techlooper.controller("salaryReviewController", function ($location, $scope, vnwConfigService, $http, jsonValue,
                                                          utils, $route, validatorService, $translate, localStorageService) {
  $scope.scroll = 0;
  $scope.email = '';
  $scope.skillBoxConfig = {
    placeholder: "mostRelevantSkills.placeholder",
    items: [],
    required: true
  };

  var state = {
    init: true,

    default: {
      showJob: true,
      order: 1,

      tabs: [
        {title: "aboutYourJob", class: "active showNavi", onClick: function (tab) {$scope.changeState(state.default);}},
        {title: "aboutYourCompany", onClick: function (tab) {$scope.changeState(state.company);}},
        {title: "yourSalaryReport", onClick: function (tab) {$scope.changeState(state.report);}}
      ],

      rootClass: "jobRoot"
    },

    company: {
      showCompany: true,
      order: 2,

      tabs: [
        {title: "aboutYourJob", class: "active", onClick: function (tab) {$scope.changeState(state.default);}},
        {
          title: "aboutYourCompany",
          class: "active showNavi",
          onClick: function (tab) {$scope.changeState(state.company);}
        },
        {title: "yourSalaryReport", onClick: function (tab) {$scope.changeState(state.report);}}
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
        {title: "aboutYourJob", class: "active", onClick: function (tab) {$scope.changeState(state.default);}},
        {title: "aboutYourCompany", class: "active", onClick: function (tab) {$scope.changeState(state.company);}},
        {
          title: "yourSalaryReport",
          class: "active showNavi",
          onClick: function (tab) {
            $scope.changeState(state.report);
          }
        }
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
    $scope.error = validatorService.validate(elems);

    $scope.salaryReview.skills = $scope.salaryReview.skills || [];
    if ($scope.salaryReview.skills.length === 0) {
      $translate("requiredThisField").then(function (trans) {$scope.error.skills = trans;});
      $scope.error.skills = true;//$translate.instant('requiredThisField');
    }

    if ($.type($scope.salaryReview.netSalary) !== "number") {
      $translate("requiredThisField").then(function (trans) {$scope.error["salaryReview.netSalary"] = trans;});
      $scope.error["salaryReview.netSalary"] = true;
    }

    if (!$.isEmptyObject($scope.error)) {
      return false;
    }

    return true;
  }

  $scope.changeState = function (st, validateCurrentState) {
    var bodyHeight = $(window).height();
    if ($('body').height() <= bodyHeight) {
      $('.navi-step-salary-review').removeClass('fixed');
    }

    st = st || state.default;
    var preferState = $.extend(true, {}, (typeof st === 'string') ? state[st] : st);
    if ($scope.forceValidation) {
      $scope.state = preferState;
      $scope.$broadcast("state change success");
      return true;
    }


    var valid = true;
    if (!state.init) {
      $.each(state.orders, function (i, stateItem) {
        if (stateItem.order > preferState.order) {
          return false;
        }
        if (stateItem.order === preferState.order && !validateCurrentState) {
          return false;
        }

        if (!$scope.validateSalaryReview(stateItem)) {
          valid = false;
          preferState = $.extend(true, {}, stateItem);
          return false;
        }
      });
    }
    delete state.init;

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

  $scope.campaign = {};

  if (!$.isEmptyObject(campaign)) {
    for (var prop in campaign) {
      try {
        campaign[prop] = JSON.parse(campaign[prop]);
      }
      catch (e) {}
    }

    $scope.campaign = campaign;

    if (campaign.id) {
      $http.get(jsonValue.httpUri.salaryReview + "/" + campaign.id)
        .success(function (data, status, headers, config) {
          $scope.salaryReview = data;
          utils.sendNotification(jsonValue.notifications.loaded);
          if ($scope.campaign.campaign === "email") {
            $scope.forceValidation = true;
            $scope.changeState(state.report);
            delete $scope.forceValidation;
          }
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

  //scope.state.demandSkills

  $scope.reload = function () {
    $route.reload();
  }

  localStorageService.set('PROMOTION-KEY', 'no');
  //localStorage.setItem('PROMOTION-KEY', 'no');
});
