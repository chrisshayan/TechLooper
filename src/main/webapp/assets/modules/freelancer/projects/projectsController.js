techlooper.controller('freelancerProjectsController', function ($scope, apiService, $timeout, resourcesService) {

  apiService.getProjects().success(function(projects){
    $scope.projects = projects;

    $timeout(function(){
      $(".project-description").dotdotdot({});
    }, 1000);
  });


  $scope.status = function (type) {
    switch (type) {
      case "show-fixed-price-fields":
        var project = arguments[1];
        if (!project) return false;
        var option = resourcesService.getOption(project.payMethod, resourcesService.paymentConfig);
        if (!option) return false;
        return option.id == "fixedPrice";

      case "show-hourly-price-fields":
        var project = arguments[1];
        if (!project) return false;
        var option = resourcesService.getOption(project.payMethod, resourcesService.paymentConfig);
        if (!option) return false;
        return option.id == "hourly";

      case "get-payment-method-translate":
        var project = arguments[1];
        if (!project) return false;
        var option = resourcesService.getOption(project.payMethod, resourcesService.paymentConfig);
        if (!option) return false;
        return option.reviewTranslate;
    }
    return false;
  }
});
