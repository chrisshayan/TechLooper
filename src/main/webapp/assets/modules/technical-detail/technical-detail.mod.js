techlooper
  .directive("technicalDetail", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/technical-detail/technical-detail.tem.html"
    }
  })
  .directive("technicalInfo", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/technical-detail/technical-info.tem.html"
    }
  })
  .directive("technicalSkills", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/technical-detail/technical-skills.tem.html"
    }
  })
  .directive("trendSkills", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/technical-detail/trend-skills.tem.html"
    }
  })
  .directive("companiesList", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/technical-detail/companies-list.tem.html"
    }
  })
  .directive("careerAlert", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/technical-detail/career-alert.tem.html"
    }
  });