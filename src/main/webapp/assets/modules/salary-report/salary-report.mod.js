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
    .directive("srSendMeReport", function ($http, $translate) {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/salary-report/sr-send-me-report.tem.html",
        link: function (scope, element, attr, ctrl) {
          scope.sendMeNow = function () {
            //var error = validatorService.validate($(".send-me-form").find('input'));
            //scope.error = error;
            //if (!$.isEmptyObject(error)) {
            //  return;
            //}

            //TODO: validation

            scope.sendMeReport.salaryReviewId = scope.salaryReview.createdDateTime;
            scope.sendMeReport.lang = $translate.use();
            $http.post("salaryReview/placeSalaryReviewReport", scope.sendMeReport)
              .success(function () {
                $('.thanks-message-for-send-me-success').addClass('show');

                delete scope.state.showSendReport;
                scope.state.showThanksSendMeReport = true;
              });
            $('.send-me-form').hide();
          }
        }
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
    .directive("srSimilarJob", function (jsonValue, connectionFactory, $timeout, $translate) {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/salary-report/sr-similar-job.tem.html",
        link: function (scope, element, attr, ctrl) {
          var timeToSends = $.extend(true, [], jsonValue.timeToSends);

          scope.doJobAlert = function () {
            $('.email-me-similar-jobs').hide();
            $('.email-similar-jobs-block').slideDown("normal");
            var jobAlert = $.extend({}, scope.salaryReview);
            jobAlert.frequency = timeToSends[0].id;
            delete jobAlert.salaryReport;
            delete jobAlert.topPaidJobs;
            scope.jobAlert = jobAlert;
          }

          scope.createJobAlert = function () {
            //var error = validatorService.validate($(".email-similar-jobs-block").find("[tl-model]"));
            //scope.error = error;
            //if (!$.isEmptyObject(error)) {
            //  return;
            //}
            //TODO validation

            var jobAlert = $.extend({}, scope.jobAlert);
            jobAlert.jobLevel = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].alertId;
            jobAlert.lang = jsonValue.languages['' + $translate.use()];
            jobAlert.salaryReviewId = jobAlert.createdDateTime;
            connectionFactory.createJobAlert(jobAlert).then(function () {
              $('.email-similar-jobs-block').slideUp("normal", function () {
                $('.success-alert-box').addClass('show');
                $timeout(function () {
                  $('.success-alert-box').removeClass('show');
                }, 2000);
              });
            });
          }
        }
      }
    });