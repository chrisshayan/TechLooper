techlooper.controller('freelancerProjectDetailController', function ($scope, utils, $location, $routeParams, apiService,
                                                                     $filter, resourcesService, localStorageService, vnwConfigService) {

  var parts = $routeParams.id.split("-");
  var projectId = parts.pop();
  projectId = parts.pop();
  apiService.getProject(projectId).success(function (data) {
    console.log(data);
    $scope.project = data.project;
    $scope.company = data.company;
    $scope.company.companySizeText= vnwConfigService.getCompanySizeText($scope.company.companySizeId);
  });


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
        var email = localStorageService.get("email") || "";
        var hasJoined = (joinProjects.indexOf(projectId) >= 0) && (email.length > 0);
        return !hasJoined;

      case "already-join":
        if (!$scope.project) return false;
        var joinProjects = localStorageService.get("joinProjects") || "";
        var email = localStorageService.get("email") || "";
        var hasJoined = (joinProjects.indexOf(projectId) >= 0) && (email.length > 0);
        return hasJoined;
    }

    return false;
  }


  $scope.joinNowByFB = function () {
    if ($scope.status('already-join')) {
      return false;
    }
    apiService.joinNowByFB();
  }

  if (localStorageService.get("joinNow")) {
    localStorageService.remove("joinNow");
    var firstName = localStorageService.get("firstName");
    var lastName = localStorageService.get("lastName");
    var email = localStorageService.get("email");
    apiService.joinProject(projectId, firstName, lastName, email, $translate.use())
      .success(function (data) {
        //if ($scope.contestDetail) {
        //  $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
        //}
        //
        var joinProjects = localStorageService.get("joinProjects") || "";
        joinProjects = joinProjects.length > 0 ? joinProjects.split(",") : [];
        if ($.inArray(projectId, joinProjects) < 0) {
          joinProjects.push(projectId);
        }

        localStorageService.set("joinProjects", joinProjects.join(","));
      });
  }
});

