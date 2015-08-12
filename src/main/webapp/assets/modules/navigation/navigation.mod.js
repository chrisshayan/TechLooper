techlooper
  .directive("navBar", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/nav-bar.html",
      controller: "navigationController"
    }
  })
  .directive("jobseekerHeader", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/jobseeker-header.tem.html"
    }
  })
  .directive("employerHeader", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/employer-header.tem.html"
    }
  });

