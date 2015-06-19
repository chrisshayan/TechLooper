techlooper.directive("srSimilarJob", function (jsonValue, connectionFactory, $timeout, $translate, validatorService, $http) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-similar-job.tem.html",
    link: function (scope, element, attr, ctrl) {
      var timeToSends = $.extend(true, [], jsonValue.timeToSends);
      var lastHideButton = undefined;
      scope.doJobAlert = function () {
        $('.email-similar-jobs-block').slideDown("normal");
        $('#txtEmailJobAlert').focus();
        delete scope.state.showJobAlertButton;
        if($('#txtEmailJobAlert').val() == ''){
          $('#txtEmailJobAlert').val(scope.$parent.email);
        }
        scope.$parent.email = scope.promotion.email;
        scope.jobAlert = angular.copy(scope.salaryReview);
        scope.jobAlert.frequency = timeToSends[0].id;
        delete scope.jobAlert.salaryReport;
        delete scope.jobAlert.topPaidJobs;
      }

      scope.hiddenJobAlertForm = function () {
        //$('.email-similar-jobs-block').hide();
        $('.email-me-similar-jobs').slideDown("normal");
        $('.email-similar-jobs-block').slideUp("normal");
        scope.state.showJobAlertButton = true;
      }

      scope.createJobAlert = function () {
        scope.error = validatorService.validate($(".email-similar-jobs-block").find("[tl-model]"));
        if (!$.isEmptyObject(scope.error)) {
          return;
        }
        var jobAlert = $.extend({}, scope.jobAlert);
        jobAlert.jobLevel = jsonValue.jobLevelsMap['' + jobAlert.jobLevelIds].alertId;
        jobAlert.lang = jsonValue.languages['' + $translate.use()];
        jobAlert.salaryReviewId = jobAlert.createdDateTime;
        jobAlert.frequency = 3;// is Weekly, @see vnwConfigService.timeToSendsSelectize
        connectionFactory.createJobAlert(jobAlert).then(function () {
          var emailVal = $('#txtEmailJobAlert');
          $('.email-similar-jobs-block').slideUp("normal");
          scope.$parent.email = emailVal.val();
          if($('#txtEmailPromotion').val() == ''){
            $('#txtEmailPromotion').val(scope.$parent.email);
          }
          if($('#txtEmailReport').val() == ''){
            $('#txtEmailReport').val(scope.$parent.email);
          }
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

      //var jobTitleSuggestion = function (jobTitle) {
      //  if (!jobTitle) {return;}
      //
      //  $http.get("suggestion/jobTitle/" + jobTitle)
      //    .success(function (data) {
      //      scope.state.jobAlertTitles = data.items.map(function (item) {return item.name;});
      //    });
      //}
      //
      //scope.$watch("jobAlert.jobTitle", function (newVal) {
      //  jobTitleSuggestion(newVal);
      //});
    }
  }
});