techlooper
  .directive("projectForm", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/freelancer/post-project/projectForm.html",
      link: function (scope, element, attr, ctrl){
        $(element).find('.date').datepicker({
          autoclose:  true,
          format: 'dd/mm/yyyy'
        });
      }
    }
  })
  .directive("projectReview", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/freelancer/post-project/reviewProject.html"
    }
  });

