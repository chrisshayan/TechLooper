techlooper
  .directive("srSurveyForm", function ($http, $translate) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-survey-form.tem.html",
      link: function (scope, element, attr, ctrl) {
        scope.survey = {};
        scope.errorFeedback = {};

        var validateFeedback = function () {
          if (scope.survey === undefined) {
            $translate("requiredThisField").then(function(trans) {
              scope.errorFeedback.understand = trans;
              scope.errorFeedback.accurate = trans;
            });
            scope.errorFeedback.understand = true;
            scope.errorFeedback.accurate = true;
          }
          else {
            if (scope.survey.isUnderstandable) {
              delete scope.errorFeedback.understand;
            }
            else {
              $translate("requiredThisField").then(function(trans) {
                scope.errorFeedback.understand = trans;
              });
              scope.errorFeedback.understand = true;
            }
            if (scope.survey.isAccurate) {
              delete scope.errorFeedback.accurate;
            }
            else {
              $translate("requiredThisField").then(function(trans) {
                scope.errorFeedback.accurate = trans;
              });
              scope.errorFeedback.accurate = true;
            }
          }
          return $.isEmptyObject(scope.errorFeedback);
        }

        scope.submitSurvey = function () {
          if (!validateFeedback()) {
            return;
          }

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
