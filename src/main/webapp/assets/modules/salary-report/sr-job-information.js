techlooper.directive("srJobInformation", function ($http) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-job-information.tem.html",
    link: function (scope, $timeout) {

      //scope.salaryReview = $.extend(true, {}, scope.salaryReview);

      scope.$watch("salaryReview", function() {
        scope.sr = $.extend(true, {}, scope.salaryReview);
      });

      var init = true;

      scope.showUpdateInfo = function () {
        init = false;
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

        ga('send', {
          'hitType': 'event',
          'eventCategory': 'editsalaryreport',
          'eventAction': 'click',
          'eventLabel': 'savebtn'
        });
      }

      scope.cancelUpdateSalaryReport = function () {
        scope.cloneSalaryReview && (scope.salaryReview = $.extend(true, {}, scope.cloneSalaryReview));
        scope.sr = $.extend(true, {}, scope.salaryReview);
        delete scope.cloneSalaryReview;
        delete scope.error;

        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      }

      var jobTitleSuggestion = function (jobTitle) {
        if (!jobTitle || init) {return;}

        $http.get("suggestion/jobTitle/" + jobTitle)
          .success(function (data) {
            scope.state.jobTitles = data.items.map(function (item) {return item.name;});
          });
      }

      scope.$watch("sr.jobTitle", function (newVal) {jobTitleSuggestion(newVal);}, true);
      scope.$watch("sr.reportTo", function (newVal) {jobTitleSuggestion(newVal);}, true);
      scope.$on("state change success", function() {
        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      });
    }
  }
});