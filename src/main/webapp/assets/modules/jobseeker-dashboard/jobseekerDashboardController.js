techlooper.controller('jobseekerDashboardController', function ($scope) {
  $scope.toggleSubmissionListing = function(){
    $('.submission-listing-block').toggle('show');
  };
  $scope.toggleScoreListing = function(){
    $('.score-details-block').toggle('show');
  };
});
