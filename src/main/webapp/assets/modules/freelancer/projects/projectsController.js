techlooper.controller('freelancerProjectsController', function ($scope, apiService, $timeout) {
  apiService.getProjects().success(function(projects){
    $scope.projects = projects;
    $timeout(function(){
      $(".project-description").dotdotdot({});
    }, 1000);
  });

});
