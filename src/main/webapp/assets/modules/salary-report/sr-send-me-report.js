techlooper.directive("srSendMeReport", function ($http, $translate, validatorService) {
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
        $('.send-me-report-form').hide();
      }
    }
  }
});