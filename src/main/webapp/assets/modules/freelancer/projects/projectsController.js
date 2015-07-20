techlooper.controller('freelancerProjectsController', function ($scope, apiService, $timeout, resourcesService) {
  apiService.getProjects().success(function(projects){
    $scope.projects = projects;
    $timeout(function(){
      $(".project-description").dotdotdot({});
    }, 1000);
    $scope.status = function (type, project) {
      switch (type) {
        case "show-fixed-price-fields":
          if (!project) return false;
          var option = resourcesService.getOption(project.payMethod, resourcesService.paymentConfig);
          if (!option) return false;
          return option.id == "fixedPrice";

        case "show-hourly-price-fields":
          if (!project) return false;
          var option = resourcesService.getOption(project.payMethod, resourcesService.paymentConfig);
          if (!option) return false;
          return option.id == "hourly";
      }
      return false;
    }
  });
});
