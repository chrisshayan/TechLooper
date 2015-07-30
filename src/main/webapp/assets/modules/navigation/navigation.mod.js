techlooper
  .directive("jobseekerHeader", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/jobseeker-header.tem.html",
      controller: "navigationController"
    }
  })
  .directive("employerHeader", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/employer-header.tem.html"
    }
  });

