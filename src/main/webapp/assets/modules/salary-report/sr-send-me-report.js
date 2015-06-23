techlooper.directive("srSendMeReport", function ($http, $translate, validatorService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-send-me-report.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.sendMeNow = function () {
        var error = validatorService.validate($(".send-me-report-form").find("[validate]:visible"));
        scope.error = error;
        if (!$.isEmptyObject(error)) {
          return;
        }

        scope.$emit("email changed", scope.sendMeReport.email);

        scope.sendMeReport.salaryReviewId = scope.salaryReview.createdDateTime;
        scope.sendMeReport.lang = $translate.use();
        $http.post("salaryReview/placeSalaryReviewReport", scope.sendMeReport);
        $('.thanks-message-for-send-me-success').addClass('show');
        scope.state.showThanksSendMeReport = true;

        delete scope.state.showSendReport;
        delete scope.sendMeReport;
      }

      scope.$on("email changed", function(event, email) {
        if (scope.sendMeReport && !scope.sendMeReport.email) {
          scope.sendMeReport.email = email;
        }
        else {
          scope.sendMeReport = {email: email};
        }
      });

      scope.$on("state change success", function() {scope.sendMeReport = {email: ""};});
    }
  }
});