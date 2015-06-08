techlooper
    .directive("srJobInformation", function () {
      return {
        restrict: "A",
        replace: true,
        templateUrl: "modules/salary-report/sr-job-information.tem.html",
        link: function(scope){
          scope.showUpdateInfo = function(){
            $('.update-job-information').removeClass('only-read');
            $('.update-info-manager').addClass('show');
            $('.ic-update-info').addClass('clicked');
          };
          $('.overlap-form').mouseenter(function(){
            $('.ic-update-info').show();
          }).mouseleave(function(){
            $('.ic-update-info').hide();
          });
          scope.updateSalaryReport = function () {
            $('.update-job-information').addClass('only-read');
            $('.update-info-manager').removeClass('show');
            $('.ic-update-info').removeClass('clicked');
          }
          scope.cancelUpdateSalaryReport = function () {
            $('.update-job-information').addClass('only-read');
            $('.update-info-manager').removeClass('show');
            $('.ic-update-info').removeClass('clicked');
          }
        }
      }
    })
    .directive("srSalaryChart", function ($translate) {
      return {
        restrict: "A",
        replace: true,
        templateUrl: "modules/salary-report/sr-salary-chart.tem.html",
        link: function (scope, element, attr, ctrl) {
          scope.openFacebookShare = function() {
            // Google Analytics Event Tracking
            ga('send', {
              'hitType': 'event',
              'eventCategory': 'facebookshare',
              'eventAction': 'salaryreport'
            });
            window.open(
              'https://www.facebook.com/sharer/sharer.php?u=' + baseUrl + '/renderSalaryReport/' + $translate.use() + '/' + scope.salaryReview.createdDateTime,
              'name', 'width=450,height=350');
          }
        }
      }
    })
    .directive("srSendMeReport", function ($http, $translate, validatorService) {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/salary-report/sr-send-me-report.tem.html",
        link: function (scope, element, attr, ctrl) {
          scope.sendMeNow = function () {
            var error = validatorService.validate($(".send-me-form").find('input'));
            scope.error = error;
            if (!$.isEmptyObject(error)) {
              return;
            }

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
    .directive("srPromotionCompany", function ($http, validatorService) {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/salary-report/sr-promotion-company.tem.html",
        link: function (scope, element, attr, ctrl) {
          scope.showPromotion = function () {
            delete scope.state.showAskPromotion;
            scope.state.showPromotionForm = true;
          }

          scope.sendCitibankPromotion = function () {
            var error = validatorService.validate($(".partner-company-form").find('input'));
            scope.error = error;
            if (!$.isEmptyObject(error)) {
              return;
            }

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
    .directive("srSimilarJob", function (jsonValue, connectionFactory, $timeout, $translate, validatorService) {
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
            $('#txtEmailJobAlert').focus();
          }
          scope.hiddenJobAlertForm = function(){
            $('.email-similar-jobs-block').hide();
            $('.email-me-similar-jobs').slideDown("normal");
          }
          scope.createJobAlert = function () {
            var error = validatorService.validate($(".email-similar-jobs-block").find("[tl-model]"));
            scope.error = error;
            if (!$.isEmptyObject(error)) {
              return;
            }
            var jobAlert = $.extend({}, scope.jobAlert);
            jobAlert.jobLevel = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].alertId;
            jobAlert.lang = jsonValue.languages['' + $translate.use()];
            jobAlert.salaryReviewId = jobAlert.createdDateTime;
            jobAlert.frequency = 3;// is Weekly, @see vnwConfigService.timeToSendsSelectize
            connectionFactory.createJobAlert(jobAlert).then(function () {
              $('.email-similar-jobs-block').slideUp("normal", function () {
                $('.success-alert-box').addClass('show');
                $timeout(function () {
                  $('.success-alert-box').removeClass('show');
                }, 2000);
              });
            });
          }

          var timeout;
          scope.$watch("jobAlert", function () {
            timeout && $timeout.cancel(timeout);
            if (!scope.jobAlert) {
              return;
            }

            timeout = $timeout(function () {
              var jobAlert = $.extend({}, scope.jobAlert);
              jobAlert.jobLevelIds = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].ids;
              connectionFactory.searchJobAlert(jobAlert).finally(null, function (data) {
                scope.jobsTotal = data.total;
              });
              timeout = undefined;
            }, 200);
          }, true);
        }
      }
    });