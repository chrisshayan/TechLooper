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
        {title: "yourSalaryReport", onClick: function(tab) {$scope.changeState(state.report);}}
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
      order: 3,

      tabs: [
        {title: "aboutYourJob", class: "active", onClick: function(tab) {$scope.changeState(state.default);}},
        {title: "aboutYourCompany", class: "active", onClick: function(tab) {$scope.changeState(state.company);}},
        {title: "yourSalaryReport", class: "active showNavi", onClick: function(tab) {$scope.changeState(state.report);}}
      ]
    }
  }

  var campaign = $location.search();

  $scope.selectize = vnwConfigService;
  $scope.salaryReview = {}

  $scope.changeState = function (st) {
    var bodyHeight = $(window).height();
    if ($('body').height() <= bodyHeight) {
      $('.navi-step-salary-review').removeClass('fixed');
    }
    
    st = st || state.default;
    var preferState = $.extend(true, {}, (typeof st === 'string') ? state[st] : st);
    if (!state.init && (preferState.order > $scope.state.order)) {
      var elems = $("." + $scope.state.rootClass).find("[validate]");
      var error = validatorService.validate(elems);

      $scope.salaryReview.skills = $scope.salaryReview.skills || [];
      if ($scope.salaryReview.skills.length === 0) {
        error.skills = $translate.instant('requiredThisField');
      }
      $scope.error = error;

      if (!$.isEmptyObject(error)) {
        return false;
      }
    }
    delete state.init;
    $scope.state = preferState;
    $scope.$emit("state change success");
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
      $scope.state.showSendReport = true;
      $scope.state.showSurvey = true;
      //$scope.changeState(state.reportSurvey);
    }
  });

  $scope.reload = function() {
    $route.reload();
  }
  localStorage.setItem('PROMOTION-KEY', 'no');

});
