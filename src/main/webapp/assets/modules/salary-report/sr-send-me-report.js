techlooper.directive("srSendMeReport", function ($http, $translate, validatorService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-send-me-report.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.sendMeNow = function () {
        var error = validatorService.validate($("#emailReport"));
        scope.error = error;
        if (!$.isEmptyObject(error)) {
          return;
        }

        scope.sendMeReport.salaryReviewId = scope.salaryReview.createdDateTime;
        scope.sendMeReport.lang = $translate.use();
        $http.post("salaryReview/placeSalaryReviewReport", scope.sendMeReport);
        $('.thanks-message-for-send-me-success').addClass('show');
        scope.state.showThanksSendMeReport = true;

        delete scope.state.showSendReport;
        delete scope.sendMeReport;
        //$('.send-me-report-form').hide();
      }
    }
  }
});