techlooper.controller('whyChallengeController', function ($scope, apiService) {
  apiService.getChallengeStatistic().success(function(data) {
    $scope.challengeStatistic = data;
  });
});
