techlooper.controller('freelancerPostProjectController', function ($scope, jsonValue, resourcesService, $rootScope, apiService, $location) {
  $scope.status = function (type) {
    switch (type) {
      case "ex-today":
        return moment().add(4, 'weeks').format(jsonValue.dateFormat);

      case "get-payment-method":
        var index = resourcesService.inOptions($scope.postProject.payMethod, resourcesService.paymentConfig);
        if (index == -1) return "";
        return resourcesService.paymentConfig.options[index];

      case "show-fixed-price-fields":
        if (!$scope.postProject) return false;
        var index = resourcesService.inOptions($scope.postProject.payMethod, resourcesService.paymentConfig);
        if (index == -1) return false;
        return resourcesService.paymentConfig.options[index].id == "fixedPrice";

      case "show-hourly-price-fields":
        if (!$scope.postProject) return false;
        var index = resourcesService.inOptions($scope.postProject.payMethod, resourcesService.paymentConfig);
        if (index == -1) return false;
        return resourcesService.paymentConfig.options[index].id == "hourly";

      case "is-form-valid":
        if (!$scope.postProjectForm) return true;
        $scope.postProjectForm.$setSubmitted();
        if ($scope.status("show-hourly-price-fields")) {
          $scope.hourlyForm.$setSubmitted();
        }
        else if ($scope.status("show-fixed-price-fields")) {
          $scope.fixedPriceForm.$setSubmitted();
        }
        return $scope.postProjectForm.$valid;

      case "show-estimate-workload":
        if (!$scope.hourly) return false;
        var workload = resourcesService.getOption($scope.hourly.estimatedWorkload, resourcesService.estimatedWorkloadConfig);
        if (!workload) return false;
        console.log(workload);
        return workload.id !== "dontKnow";
    }
  }

  var state = {
    default: {
      showPostProjectForm: true
    },

    review: {
      showPostProjectReview: true
    }
  }

  $scope.changeState = function (st) {
    if (!st || ($scope.state && !$scope.status("is-form-valid"))) {
      return false;
    }

    var pState = angular.copy(state[st] || st);
    $scope.state = pState;
  }

  $scope.$watch("postProject.payMethod", function (newVal, oldVal) {
    if ($scope.status("show-hourly-price-fields")) {
      $scope.hourlyForm.$setPristine();
      $rootScope.$emit("$setPristine");
      $scope.hourly = {}
    }
    else if ($scope.status("show-fixed-price-fields")) {
      $scope.fixedPriceForm.$setPristine();
      $rootScope.$emit("$setPristine");
      $scope.fixedPrice = {}
    }
  });

  $scope.createProject = function () {
    if ($scope.status("show-hourly-price-fields")) {
      $scope.hourlyForm.$setSubmitted();
    }
    if ($scope.status("show-fixed-price-fields")) {
      $scope.fixedPriceForm.$setSubmitted();
    }
    if ($scope.postProjectForm.$invalid) {
      return;
    }

    var postProject = $.extend(true, {}, $scope.hourly, $scope.fixedPrice, $scope.postProject);
    //TODO : send to server
    apiService.postFreelancerProject(postProject)
      .success(function (projectId) {
        $location.path("/hiring");
      });
  }

  $scope.changeState('default');
});
