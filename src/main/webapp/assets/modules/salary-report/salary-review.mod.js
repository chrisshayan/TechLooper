techlooper
  .directive("srNavigation", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-navigation.tem.html"
    }
  })
  .directive("srAboutYourReport", function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-report.tem.html"
    }
  })
  .directive("srAboutYourCompany", function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-company.tem.html"
    }
  })
  .directive("srAboutYourJob", function ($translate, vnwConfigService, $compile) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-job.tem.html",
      link: function (scope, element, attr, ctrl) {
      }
    }
  })
  .directive("srJobInformation", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-job-information.tem.html"
    }
  })
  .directive("srSalaryChart", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-salary-chart.tem.html"
    }
  })
  .directive("srSendMeReport", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-send-me-report.tem.html"
    }
  })
  .directive("srPromotionCompany", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-promotion-company.tem.html"
    }
  })
  .directive("srSurveyForm", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-survey-form.tem.html"
    }
  })
  .directive("srSimilarJob", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-similar-job.tem.html"
    }
  });