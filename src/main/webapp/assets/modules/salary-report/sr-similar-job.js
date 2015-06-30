techlooper.directive("srSimilarJob", function (jsonValue, connectionFactory, $timeout, $translate, validatorService, focus) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-similar-job.tem.html",
    link: function (scope, element, attr, ctrl) {
      var timeToSends = $.extend(true, [], jsonValue.timeToSends);

      var emailSuggestion = "";

      scope.doJobAlert = function ($event) {
        $event.preventDefault();

        $('.email-similar-jobs-block').show();
        focus("emailJobAlert");

        if (scope.jobAlert) {
          emailSuggestion =  scope.jobAlert.email || "";
        }
        scope.jobAlert = angular.copy(scope.salaryReview);
        scope.jobAlert.frequency = timeToSends[0].id;
        if (!scope.jobAlert.email) {
          emailSuggestion.length > 0 && (scope.jobAlert.email = emailSuggestion);
        }
        emailSuggestion = "";

        delete scope.state.showJobAlertButton;
        delete scope.jobAlert.salaryReport;
        delete scope.jobAlert.topPaidJobs;
      };

      scope.hiddenJobAlertForm = function () {
        //$('.email-similar-jobs-block').hide();
        $('.email-me-similar-jobs').show();
        $('.email-similar-jobs-block').hide();
        scope.state.showJobAlertButton = true;
      }

      scope.createJobAlert = function () {
        var emailVal = $('#txtEmailJobAlert');
        //scope.$parent.email = emailVal.val();
        scope.jobAlert.email = emailVal.val();
        scope.error = validatorService.validate($(".email-similar-jobs-block").find("[tl-model]"));
        if (!$.isEmptyObject(scope.error)) {
          return;
        }

        var jobAlert = $.extend({}, scope.jobAlert);
        jobAlert.jobLevel = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].alertId;
        jobAlert.lang = jsonValue.languages['' + $translate.use()];
        jobAlert.salaryReviewId = jobAlert.createdDateTime;
        jobAlert.frequency = 3;// is Weekly, @see vnwConfigService.timeToSendsSelectize

        scope.$emit("email changed", scope.jobAlert.email);

        connectionFactory.createJobAlert(jobAlert).then(function () {
          $('.email-similar-jobs-block').hide();
        });
        scope.state.showJobAlertThanks = true;
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

      scope.$on("email changed", function (event, email) {
        if (scope.jobAlert && !scope.jobAlert.email) {
          scope.jobAlert.email = email;
        }
        else {
          emailSuggestion = email;
        }
      });

      scope.$on("state change success", function() {emailSuggestion = "";})
    }
  }
});