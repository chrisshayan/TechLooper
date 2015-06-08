techlooper.directive("srJobInformation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/salary-report/sr-job-information.tem.html",
    link: function (scope, $timeout) {
      scope.showUpdateInfo = function () {
        $('.update-job-information').removeClass('only-read');
        $('.ic-update-info').addClass('clicked');
      };
      $('.overlap-form').mouseenter(function () {
        $('.ic-update-info').show();
      }).mouseleave(function () {
        $('.ic-update-info').hide();
      });
      scope.updateSalaryReport = function () {
        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      }
      scope.cancelUpdateSalaryReport = function () {
        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      }
    }
  }
});