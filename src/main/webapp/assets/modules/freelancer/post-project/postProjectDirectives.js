techlooper
  .directive("projectForm", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/freelancer/post-project/projectForm.html"
    }
  })
  .directive("projectReview", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/freelancer/post-project/reviewProject.html"
    }
  });

