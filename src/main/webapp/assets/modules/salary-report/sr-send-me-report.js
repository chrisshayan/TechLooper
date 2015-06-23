techlooper.directive("srSendMeReport", function ($http, $translate, validatorService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-send-me-report.tem.html",
    link: function (scope, element, attr, ctrl) {
      var emailVal = $('#txtEmailReport');
      if(emailVal.val() ==''){
        emailVal.val(scope.$parent.email);
      }
      scope.sendMeNow = function () {
        var error = validatorService.validate($(".send-me-report-form").find("[validate]:visible"));
        scope.error = error;
        if (!$.isEmptyObject(error)) {
          return;
        }
        scope.$parent.email = emailVal.val();
        if($('#txtEmailJobAlert').val() == ''){
          $('#txtEmailJobAlert').val(scope.$parent.email);
        }
        if($('#txtEmailPromotion').val() == ''){
          $('#txtEmailPromotion').val(scope.$parent.email);
        }
        scope.sendMeReport.salaryReviewId = scope.salaryReview.createdDateTime;
        scope.sendMeReport.lang = $translate.use();
        $http.post("salaryReview/placeSalaryReviewReport", scope.sendMeReport);
        $('.thanks-message-for-send-me-success').addClass('show');
        scope.state.showThanksSendMeReport = true;

        delete scope.state.showSendReport;
        delete scope.sendMeReport;
      }
    }
  }
});