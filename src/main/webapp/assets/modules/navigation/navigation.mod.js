techlooper
  .directive("navBar", function () {
    return {
      restrict: "E",
      replace: true,
      scope: {},
      templateUrl: "modules/navigation/nav-bar.html",
      controller: "navigationController"
    }
  })
  .directive("jobseekerHeader", function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/jobseeker-header.tem.html",
      controller: "navigationController"
    }
  })
  .directive("employerHeader", function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/navigation/employer-header.tem.html"
    }
  })
  .directive("footer", function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/footer/footer.html"
    }
  });

