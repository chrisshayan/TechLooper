techlooper.directive("getPromotedForm", function ($http) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html",
    link: function (scope) {
      var jobTitleSuggestion = function (jobTitle) {
        if (!jobTitle) {
          return;
        }

        $http.get("suggestion/jobTitle/" + jobTitle)
          .success(function (data) {
            scope.jobTitles = data.items.map(function (item) {return item.name;});
          });
      }
      scope.$watch("getPromoted.jobTitle", function (newVal) {
        jobTitleSuggestion(newVal);
      });
    }
  }
}).directive("getPromotedResults", function ($http, validatorService, $translate) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-result.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.sendMeNow = function () {
        var error = validatorService.validate($(".send-me-skills-trend").find("[validate]:visible"));
        scope.error = error;
        if (!$.isEmptyObject(error)) {
          return;
        }

        //scope.sendMeReport.salaryReviewId = scope.salaryReview.createdDateTime;
        //scope.sendMeReport.lang = $translate.use();
        //$http.post("salaryReview/placeSalaryReviewReport", scope.sendMeReport);
        scope.showThanksSendMeDemandedSkills = true;
        //
        delete scope.showSendDemandedSkills;
        //delete scope.sendMeReport;
      }
    }
  }
});