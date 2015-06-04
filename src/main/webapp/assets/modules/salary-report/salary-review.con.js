techlooper.controller("salaryReviewController", function ($location, $scope, vnwConfigService, $http, jsonValue, utils) {
  var state = {
    init: true,

    default: {
      showJob: true,
      skillBoxConfig: {
        placeholder: "mostRelevantSkills.placeholder",
        items: [],
        required: true
      },

      tabs: [
        {title: "aboutYourJob", class: "active"},
        {title: "aboutYourCompany"},
        {title: "yourSalaryReport"}
      ]
    },

    company: {
      showCompany: true,

      tabs: [
        {title: "aboutYourJob", class: "active"},
        {title: "aboutYourCompany", class: "active"},
        {title: "yourSalaryReport"}
      ]
    },

    report: {
      showReport: true,
      ableCreateNewReport: true,

      tabs: [
        {title: "aboutYourJob", class: "active"},
        {title: "aboutYourCompany", class: "active"},
        {title: "yourSalaryReport", class: "active"}
      ]
    }
  }

  var campaign = $location.search();

  $scope.selectize = vnwConfigService;

  $scope.changeState = function (st) {
    st = st || state.default;
    var preferState = $.extend(true, {}, (typeof st === 'string') ? state[st] : st);
    if (state.init) {
      //TODO: check validation and return if any error
    }
    delete state.init;
    $scope.state = preferState;
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
          $scope.salaryReview.campaign = !$.isEmptyObject(campaign);
          utils.sendNotification(jsonValue.notifications.loaded);
        });
    }
    else {
      $scope.salaryReview = campaign;
    }
  }

  $scope.changeState(preferState);

  $scope.$watch("salaryReport", function (salaryReport) {
    if (!salaryReport) {return;}

    if ($.type(salaryReport.percentRank) === "number") {
      $scope.state.showSendReport = true;
      $scope.state.showSurvey = true;
      //$scope.changeState(state.reportSurvey);
    }
  });
});
