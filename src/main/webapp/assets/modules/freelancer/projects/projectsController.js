techlooper.controller('freelancerProjectsController', function ($scope, apiService) {

  apiService.getProjects().success(function(projects){
    $scope.projects = projects;
    console.log($scope.projects);
  });
});
