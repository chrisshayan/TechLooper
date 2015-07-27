techlooper.controller('whyFreelancerController', function ($scope, apiService) {
  apiService.getProjectStatistic().success(function(data) {
    $scope.projectStatistic = data;
    console.log($scope.projectStatistic);
  });
});
