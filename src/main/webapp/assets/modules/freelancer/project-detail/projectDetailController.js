techlooper.controller('freelancerProjectDetailController', function ($scope, utils, $location, $routeParams, apiService,
                                                                     $filter, resourcesService, localStorageService,
                                                                     $translate, vnwConfigService, jsonValue) {
  //$('.loading-data').css("height", $(window).height());
  //$('body').addClass('noscroll');
  //utils.sendNotification(jsonValue.notifications.loading);

  var parts = $routeParams.id.split("-");
  var lastPart = parts.pop();
  if (parts.length < 2 || (lastPart !== "id")) {
    return $location.path("/");
  }

  var projectId = parts.pop();
  var title = parts.join("");
  if (utils.hasNonAsciiChar(title)) {
    title = utils.toAscii(title);
    return $location.url(sprintf("/freelancer/project-detail/%s-%s-id", title, projectId));
  }

  apiService.getProject(projectId).success(function (data) {
    $scope.project = data.project;
    $scope.company = data.company;
    if ($scope.company) {
      $scope.company.companySizeText = vnwConfigService.getCompanySizeText($scope.company.companySizeId);
    }
    utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
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

      case "expired-project":
        if (!$scope.project) return false;
        var expired = $scope.status("show-fixed-price-fields") && $scope.status("show-fixed-price-fields") && moment().isAfter(moment($scope.project.estimatedEndDate, jsonValue.dateFormat), "day");
        return expired;

      case "able-to-join":
        if (!$scope.project) return false;
        var joinProjects = localStorageService.get("joinProjects") || "";
        var email = localStorageService.get("email") || "";
        var hasJoined = (joinProjects.indexOf(projectId) >= 0) && (email.length > 0);
        var expired = $scope.status("expired-project");
        return !expired && !hasJoined;

      case "apply-button-title":
        if ($scope.status("expired-project")) return "thisJobIsExpired";
        return "applyWithFacebook";

      case "already-join":
        if (!$scope.project) return false;
        var joinProjects = localStorageService.get("joinProjects") || "";
        var email = localStorageService.get("email") || "";
        var hasJoined = (joinProjects.indexOf(projectId) >= 0) && (email.length > 0);
        return expired || hasJoined;

      case "disable-apply-button":
        return $scope.showPostSuccessfulMessage || $scope.status('already-join') || $scope.status('expired-project');
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
    $scope.freelancer = $scope.freelancer || {};
    $scope.freelancer = $.extend(true, {}, $scope.freelancer, {
      firstName: localStorageService.get("firstName"),
      lastName: localStorageService.get("lastName"),
      email: localStorageService.get("email")
    });
    localStorageService.remove("joinNow");
    $("#applyJob").modal();
  }

  $scope.joinProject = function () {
    $scope.freelancerForm.$setSubmitted();
    if ($scope.freelancerForm.$invalid) {
      return false;
    }

    localStorageService.set("email", $scope.freelancer.email);
    apiService.joinProject(projectId, $scope.freelancer.firstName, $scope.freelancer.lastName,
      $scope.freelancer.email, $scope.freelancer.phoneNumber, $scope.freelancer.resumeLink, $translate.use())
      .success(function (numberFBJoins) {
        //$scope.numberFBJoins = numberFBJoins;
        if ($scope.project) {
          $scope.project.numberOfApplications = numberFBJoins;
        }
        var joinProjects = localStorageService.get("joinProjects") || "";
        joinProjects = joinProjects.length > 0 ? joinProjects.split(",") : [];
        if ($.inArray(projectId, joinProjects) < 0) {
          joinProjects.push(projectId);
        }
        localStorageService.set("joinProjects", joinProjects.join(","));
      });
    $("#applyJob").modal("hide");
  }

  if (localStorageService.get("postProject") == true) {
    localStorageService.remove("postProject");
    $scope.showPostSuccessfulMessage = true;
  }

  $scope.fbShare = function () {
    ga("send", {
      hitType: "event",
      eventCategory: "facebookshare",
      eventAction: "click",
      eventLabel: "freelancerdetail"
    });
    utils.openFBShare("/shareFreelancerProject/" + $translate.use() + "/" + projectId);
  }
});

