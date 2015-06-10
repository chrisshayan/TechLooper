techlooper.directive("srAboutYourJob", function ($http, validatorService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-about-your-job.tem.html",
    link: function (scope, element, attr, ngModel) {
      var jobTitleSuggestion = function (jobTitle) {
        if (!jobTitle) {
          return;
        }

        $http.get("suggestion/jobTitle/" + jobTitle)
          .success(function (data) {
            scope.state.jobTitles = data.items.map(function (item) {return item.name;});
          });
      }

      scope.$watch("salaryReview.jobTitle", function (newVal) {
        jobTitleSuggestion(newVal);
      });
      scope.$watch("salaryReview.reportTo", function (newVal) {jobTitleSuggestion(newVal);}, true);

      scope.$watch("state.skillBoxConfig.newTag", function (newVal) {
        var els = $('[ng-model="config.newTag"]').find('input');
        if (!newVal) {
          els.find('ul').addClass('ng-hide');
          return;
        }else{
          els.find('ul').removeClass('ng-hide');
        }

        $http.get("suggestion/skill/" + newVal)
          .success(function (data) {
            scope.state.skillBoxConfig.items = data.items.map(function (item) {return item.name;});
          });
      });
    }
  }
});