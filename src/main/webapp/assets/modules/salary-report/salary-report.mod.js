techlooper
    .directive("srJobInformation", function () {
      return {
        restrict: "A",
        replace: true,
        templateUrl: "modules/salary-report/sr-job-information.tem.html"
      }
    })
    .directive("srSalaryChart", function () {
      return {
        restrict: "A",
        replace: true,
        templateUrl: "modules/salary-report/sr-salary-chart.tem.html"
      }
    })
    .directive("srSendMeReport", function () {
      return {
        restrict: "A",
        replace: true,
        templateUrl: "modules/salary-report/sr-send-me-report.tem.html"
      }
    })
    .directive("srPromotionCompany", function ($http) {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/salary-report/sr-promotion-company.tem.html",
        link: function (scope, element, attr, ctrl) {
          //TODO validation

          scope.showPromotion = function () {
            delete scope.state.showAskPromotion;
            scope.state.showPromotionForm = true;
          }

          scope.sendCitibankPromotion = function () {
            //TODO validation

            if (scope.promotion.paymentMethod !== 'BANK_TRANSFER') {
              scope.state.showThanksCash = true;
              return;
            }

            scope.promotion.salaryReviewId = scope.salaryReview.createdDateTime;
            $http.post("promotion/citibank/creditCard", scope.promotion)
                .success(function () {
                  localStorage.setItem('PROMOTION-KEY', 'yes');
                });

            delete scope.state.showPromotion;
            scope.state.showThanksBankTransfer = true;
          }
        }
      }
    })
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
                  delete scope.state.showSurvey;
                  scope.state.showThanksSurvey = true;
                });
            $('.email-get-similar-jobs').focus();
          }
        }
      }
    })
    .directive("srSimilarJob", function () {
      return {
        restrict: "A",
        replace: true,
        templateUrl: "modules/salary-report/sr-similar-job.tem.html"
      }
    });