techlooper
  .directive("srNavigation", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-navigation.tem.html"
    }
  })
  .directive("srAboutYourJob", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-job.tem.html"
    }
  })
  .directive("srAboutYourCompany", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-company.tem.html"
    }
  })
  .directive("srAboutYourReport", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-report.tem.html"
    }
  });