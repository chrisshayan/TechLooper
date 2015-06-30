techlooper
  .directive("srSurveyForm", function ($http, $translate) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-survey-form.tem.html",
      link: function (scope, element, attr, ctrl) {
        scope.survey = {};
        var validateFeedback = function () {
          if (scope.survey === undefined) {
            $translate("requiredThisField").then(function(trans) {
              scope.error.understand = trans;
              scope.error.accurate = trans;
            });
            scope.error.understand = true;
            scope.error.accurate = true;
          }
          else {
            if (scope.survey.isUnderstandable) {
              delete scope.error.understand;
            }
            else {
              $translate("requiredThisField").then(function(trans) {
                scope.error.understand = trans;
              });
              scope.error.understand = true;
            }
            if (scope.survey.isAccurate) {
              delete scope.error.accurate;
            }
            else {
              $translate("requiredThisField").then(function(trans) {
                scope.error.accurate = trans;
              });
              scope.error.accurate = true;
            }
          }
          return $.isEmptyObject(scope.error);
        }

        scope.submitSurvey = function () {
          if (!validateFeedback()) {
            return;
          }

          scope.survey.salaryReviewId = scope.salaryReview.createdDateTime;
          $http.post("saveSalaryReviewSurvey", scope.survey)
            .success(function (data, status, headers, config) {
             delete scope.state.showSurvey;
             delete scope.survey.isUnderstandable;
             delete scope.survey.isAccurate;
             $('#txtFeedback').val('');

             scope.state.showThanksSurvey = true;
            });
          $('.email-get-similar-jobs').focus();
        }
      }
    }
  });
