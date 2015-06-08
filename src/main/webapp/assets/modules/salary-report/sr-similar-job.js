techlooper.directive("srSimilarJob", function (jsonValue, connectionFactory, $timeout, $translate, validatorService) {
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