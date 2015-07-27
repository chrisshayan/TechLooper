techlooper.controller('whyFreelancerController', function ($scope) {
  apiService.getProjectStatistic().success(function(data) {
    $scope.projectStatistic = data;
  });
});
