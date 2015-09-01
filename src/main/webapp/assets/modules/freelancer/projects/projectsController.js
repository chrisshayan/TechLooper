techlooper.controller('freelancerProjectsController', function ($scope, apiService, $timeout, resourcesService, jsonValue, utils) {
  $('.loading-data').css("height", $(window).height());
  $('body').addClass('noscroll');
  utils.sendNotification(jsonValue.notifications.loading);
  apiService.getProjects().success(function(projects){
    $scope.projects = projects;
  });
  $scope.$on('addDotDotDot', function() {
    $(".project-description").dotdotdot({});
    $(".project-description").css('height', 'auto');
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
