techlooper.controller('freelancerProjectDetailController', function ($scope, utils, $location, $routeParams, apiService,
                                                                     $filter, resourcesService, localStorageService) {

  $scope.status = function (type) {
    switch (type) {
      case "show-fixed-price-fields":
        if (!$scope.project) return false;
        var option = resourcesService.getOption($scope.project.payMethod, resourcesService.paymentConfig);
        if (!option) return false;
        return option.id == "fixedPrice";

      case "show-hourly-price-fields":
        if (!$scope.project) return false;
        var option = resourcesService.getOption($scope.project.payMethod, resourcesService.paymentConfig);
        if (!option) return false;
        return option.id == "hourly";

      case "show-estimate-workload":
        if (!$scope.project) return false;
        var workload = resourcesService.getOption($scope.project.estimatedWorkload, resourcesService.estimatedWorkloadConfig);
        if (!workload) return false;
        return workload.id !== "dontKnow";

      case "able-to-join":
      if (!$scope.project) return false;
      var joinProjects = localStorageService.get("joinProjects") || "";
      //var email = localStorageService.get("email") || "";
      //var contestInProgress = ($scope.contestDetail.progress.translate == jsonValue.status.registration.translate) ||
      //  ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
      //var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
      //return contestInProgress && !hasJoined;

      case "already-join":
      //if (!$scope.contestDetail) return false;
      //var joinContests = localStorageService.get("joinContests") || "";
      //var email = localStorageService.get("email") || "";
      //var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
      //return !hasJoined;
    }

    return false;
  }

  var parts = $routeParams.id.split("-");
  var projectId = parts.pop();
  projectId = parts.pop();
  apiService.getProject(projectId).success(function (data) {
    $scope.project = data.project;
    $scope.company = data.company;
  });

  $scope.joinNowByFB = function () {
    if (!$scope.status('able-to-join')) {
      return false;
    }

    localStorageService.set("lastFoot", $location.url());
    apiService.getFBLoginUrl().success(function (url) {
      localStorageService.set("lastFoot", $location.url());
      localStorageService.set("joinNow", true);
      window.location = url;
    });
  }
});

