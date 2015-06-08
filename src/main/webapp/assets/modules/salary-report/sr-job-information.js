techlooper.directive("srJobInformation", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-job-information.tem.html",
    link: function (scope, $timeout) {

      //scope.salaryReview = $.extend(true, {}, scope.salaryReview);

      scope.$watch("salaryReview", function() {
        scope.sr = $.extend(true, {}, scope.salaryReview);
      });

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
        scope.cloneSalaryReview = $.extend(true, {}, scope.salaryReview);
        scope.salaryReview = $.extend(true, {}, scope.sr);

        if (scope.changeState("report")) {
          $('.update-job-information').addClass('only-read');
          $('.ic-update-info').removeClass('clicked');
        }
      }

      scope.cancelUpdateSalaryReport = function () {
        console.log(scope.salaryReview);
        scope.cloneSalaryReview && (scope.salaryReview = $.extend(true, {}, scope.cloneSalaryReview));
        scope.sr = $.extend(true, {}, scope.salaryReview);
        delete scope.cloneSalaryReview;
        delete scope.error;

        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      }
    }
  }
});