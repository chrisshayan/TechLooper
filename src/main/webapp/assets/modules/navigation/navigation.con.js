techlooper.controller("navigationController", function ($scope, securityService, apiService, localStorageService, $location,
                                                        jsonValue, utils, $timeout, $rootScope) {
  console.log($rootScope.userInfo);

  $scope.state = function (type) {
    switch (type) {
      case "home-url":
        if ($rootScope.userInfo) {
          if ($rootScope.userInfo.roleName === "JOB_SEEKER") {
            return "/home";
          }
          return "/hiring";
        }
        return "/home";

      case "employer-signed-in":
        if (!$rootScope.userInfo) {
          switch (utils.getView()) {
            case jsonValue.views.hiring:
            case jsonValue.views.whyChallenge:
            case jsonValue.views.whyFreelancer:
            case jsonValue.views.priceJob:
              return true;
          }
          return false;
        }
        return $rootScope.userInfo.roleName === "EMPLOYER";

      case "job-seeker-signed-in":
        if (!$rootScope.userInfo) {
          switch (utils.getView()) {
            case jsonValue.views.home:
            case jsonValue.views.pieChart:
            case jsonValue.views.salaryReview:
            case jsonValue.views.getPromoted:
            case jsonValue.views.contest:
            case jsonValue.views.challenges:
            case jsonValue.views.challengeDetail:
            case jsonValue.views.freelancerProjectDetail:
            case jsonValue.views.freelancerProjects:
            case jsonValue.views.howItWorks:
            case jsonValue.views.analyticsSkill:
              return true;
          }
          return false;
        }
        return $rootScope.userInfo.roleName === "JOB_SEEKER";

      case "navBarHided":
        return !($scope.state("employer-signed-in") && $scope.state("job-seeker-signed-in"));

      case "current-view":
        return utils.getView();
    }
  }

  $scope.logout = function () {
    securityService.logout();
    localStorageService.remove("social");
  }

  var code = localStorageService.get("code");
  if (code) {
    utils.sendNotification(jsonValue.notifications.loading, $(window).height());
    securityService.login(code, localStorageService.get("social"), "social").success(function (data) {
      securityService.getCurrentUser("social");
    }).finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});
    localStorageService.remove("code");
    return;
  }

  securityService.getCurrentUser(localStorageService.get("social") ? "social" : undefined);

  $scope.isActive = function (viewLocation) {
    var active = (viewLocation === $location.path());
    return active;
  };
  $scope.hideMenu = function () {
    if (utils.isMobile() == true) {
      $scope.mobileMenuEmployer = !$scope.mobileMenuEmployer;
      $scope.mobileMenu = !$scope.mobileMenu;
    }
  }
});
