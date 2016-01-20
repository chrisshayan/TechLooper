techlooper.controller('jobseekerDashboardController', function ($scope, apiService, $compile) {

  apiService.getJobseekerDashboard()
    .success(function (info) {
      $scope.dashboardInfo = info;

    });

  $scope.toggleSubmissions = function (challenge) {
    $("[class^='challenge-toggle-']").html("");

    if (challenge.$toggle.isToggleSubmissions()) {
      return challenge.$toggle.reset();
    }

    challenge.$toggle.toggleSubmissions();
    $scope.$challenge = challenge;
    var html = "<jobseeker-submissions></jobseeker-submissions>";
    var compiled = $compile(html)($scope);
    $(".challenge-toggle-submissions-" + challenge.challengeId).html(compiled);
  };

  $scope.toggleCriteria = function (challenge) {
    $("[class^='challenge-toggle-']").html("");

    if (challenge.$toggle.isToggleCriteria()) {
      return challenge.$toggle.reset();
    }

    challenge.$toggle.toggleCriteria();
    $scope.$challenge = challenge;
    var html = "<jobseeker-criteria></jobseeker-criteria>";
    var compiled = $compile(html)($scope);
    $(".challenge-toggle-criteria-" + challenge.challengeId).html(compiled);
  };

  //challengeId: 1449129241321
  //challengeName: "Test send mail update criteria"
  //currentPhase: "FINAL"
  //currentPhaseSubmissionDate: "27/12/2015"
  //disqualified: null
  //numberOfSubmissions: 2
  //prize: null
  //rank: null
  //score: 0
  //submissionDate: "27/12/2015"
});
