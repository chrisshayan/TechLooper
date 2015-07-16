techlooper
  .directive("projectForm", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/freelancer/post-project/projectForm.html",
      link: function (scope, element, attr, ctrl){
        $(element).find('[data-toggle="tooltip"]').tooltip({
          html: true,
          placement: 'right',
          'trigger': "focus",
          animation: true
        });
        var nowDate = new Date();
        var today = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate(), 0, 0, 0, 0);
        $(element).find('.date').datepicker({
          autoclose:  true,
          format: 'dd/mm/yyyy',
          startDate: today
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

