techlooper.controller("navigationController", function ($scope, securityService, apiService, localStorageService, $location,
                                                        jsonValue, utils, $timeout, $rootScope, $window) {
  $scope.state = function (type) {
    switch (type) {
      case "show-employer-header":
        var isEmployerView = $rootScope.currentUiView && $rootScope.currentUiView.header === "EMPLOYER";
        var isEmployer = $rootScope.userInfo && $rootScope.userInfo.roleName === "EMPLOYER";
        return isEmployerView || isEmployer;

      case "show-job-seeker-header":
        var employerHeaderNotShown = !$scope.state("show-employer-header");
        var isJobSeekerView = $rootScope.currentUiView && $rootScope.currentUiView.type != "LOGIN";
        return employerHeaderNotShown && isJobSeekerView;


      case "home-url":
        var isEmployer = $rootScope.userInfo && $rootScope.userInfo.roleName === "EMPLOYER";
        //if ($rootScope.userInfo) {
        //  if ($rootScope.userInfo.roleName === "JOB_SEEKER") {
        //    return "home";
        //  }
        //  return "hiring";
        //}
        return isEmployer ? "hiring" : "home";

      //case "employer-signed-in":
      //  if (!$rootScope.userInfo) {
      //    switch (utils.getView()) {
      //      case jsonValue.views.hiring:
      //      case jsonValue.views.whyChallenge:
      //      case jsonValue.views.whyFreelancer:
      //      case jsonValue.views.priceJob:
      //        return true;
      //    }
      //    return false;
      //  }
      //  return $rootScope.userInfo.roleName === "EMPLOYER";

      //case "job-seeker-signed-in":
      //  if (!$rootScope.userInfo) {
      //    switch (utils.getView()) {
      //      case jsonValue.views.login:
      //      case jsonValue.views.userType:
      //        return false;
      //    }
      //
      //    if ($scope.state("employer-signed-in")) {
      //      return false;
      //    }
      //    return true;
      //  }
      //  return $rootScope.userInfo.roleName === "JOB_SEEKER";

      //case "navBarHided":
      //  return !($scope.state("employer-signed-in") && $scope.state("job-seeker-signed-in"));

      //case "current-view":
      //  return utils.getView();
    }
  }

  $scope.logout = function () {
    securityService.logout();
    localStorageService.remove("social");
  }

  //var code = localStorageService.get("code");
  //if (code) {
  //  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  //  securityService.login(code, localStorageService.get("social"), "social").success(function (data) {
  //    securityService.getCurrentUser("social");
  //  }).finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});
  //  localStorageService.remove("code");
  //  return;
  //}
  //
  //securityService.getCurrentUser();

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

  $scope.changeLanguages = function(key){
    $scope.langKey = key;
    localStorage.NG_TRANSLATE_LANG_KEY = key;
    $window.location.reload();
  }
});
