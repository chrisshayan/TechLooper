techlooper
  .directive("srSurveyForm", function ($http, jsonValue) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-survey-form.tem.html",
      link: function (scope, element, attr, ctrl) {
        scope.submitSurvey = function () {
          //TODO validation

          scope.survey.salaryReviewId = scope.salaryReview.createdDateTime;
          $http.post("saveSalaryReviewSurvey", scope.survey)
            .success(function (data, status, headers, config) {
             // delete scope.state.showSurvey;
                $('.salary-report-feedback-form').hide();
              scope.state.showThanksSurvey = true;
            });
          $('.email-get-similar-jobs').focus();
        }
      }
    }
  });
