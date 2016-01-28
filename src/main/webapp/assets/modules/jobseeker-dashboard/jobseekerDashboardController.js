techlooper.controller('jobseekerDashboardController', function ($scope, apiService, $compile) {

  apiService.getJobseekerDashboard()
    .success(function (info) {
      $scope.dashboardInfo = info;

    });

  $scope.toggleSubmissions = function (challenge) {
    var challengeItem = $('.challenge-' + challenge.challengeId);
    if(challengeItem.find('.submission-col .fa-caret-down').hasClass('fa-caret-up')){
      challengeItem.find('.challenge-toggle-submissions').html("");
      challengeItem.find('.fa-caret-down').removeClass('fa-caret-up');
    }else{
      $("[class^='challenge-toggle-']").html("");
      $('.my-challenge-list').find('.fa-caret-down').removeClass('fa-caret-up');

      $scope.$challenge = challenge;
      var html = "<jobseeker-submissions></jobseeker-submissions>";
      var compiled = $compile(html)($scope);
      challengeItem.find('.challenge-toggle-submissions').html(compiled);
      challengeItem.find('.submission-col .fa-caret-down').addClass('fa-caret-up');
    }
  };

  $scope.toggleCriteria = function (challenge) {

    var challengeItem = $('.challenge-' + challenge.challengeId);
    if(challengeItem.find('.score-col .fa-caret-down').hasClass('fa-caret-up')){
      challengeItem.find('.challenge-toggle-criteria').html("");
      challengeItem.find('.fa-caret-down').removeClass('fa-caret-up');
    }else{
      $("[class^='challenge-toggle-']").html("");
      $('.my-challenge-list').find('.fa-caret-down').removeClass('fa-caret-up');

      $scope.$challenge = challenge;
      var html = "<jobseeker-criteria></jobseeker-criteria>";
      var compiled = $compile(html)($scope);
      challengeItem.find('.challenge-toggle-criteria').html(compiled);
      challengeItem.find('.score-col .fa-caret-down').addClass('fa-caret-up');
    }
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
