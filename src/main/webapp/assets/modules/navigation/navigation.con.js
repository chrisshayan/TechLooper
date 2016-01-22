techlooper.controller("navigationController", function ($scope, securityService, apiService, localStorageService, $location,
                                                        jsonValue, utils, $timeout, $rootScope, $window) {
  $scope.state = function (type) {
    var isEmployer = $rootScope.userInfo && $rootScope.userInfo.roleName === "EMPLOYER";
    var isJobSeeker = $rootScope.userInfo && $rootScope.userInfo.roleName === "JOB_SEEKER";
    //var userNotLogin = !$rootScope.userInfo;
    var currentViewIsLogin = $rootScope.currentUiView && $rootScope.currentUiView.isLoginPage;
    var isEmployerView = $rootScope.currentUiView && $rootScope.currentUiView.header == "EMPLOYER";
    //var isJobSeekerView = $rootScope.currentUiView &&  $rootScope.currentUiView.header != "EMPLOYER" && $rootScope.currentUiView.type != "LOGIN";
    //console.log(isJobSeekerView);
    switch (type) {
      case "show-employer-header":
        if (currentViewIsLogin || isJobSeeker) return false;
        if (isEmployer) return true;
        //isEmployer = $rootScope.userInfo && $rootScope.userInfo.roleName === "EMPLOYER";
        //console.log("emm", isEmployerView);
        return isEmployerView;
      //return (!currentViewIsLogin && isEmployer) || isEmployerView;

      case "show-job-seeker-header":
        if (currentViewIsLogin || isEmployer) return false;
        if (isJobSeeker) return true;
        if (isEmployerView) return false;
        //var employerHeaderNotShown = !$scope.state("show-employer-header");
        //var isEmployerView = $rootScope.currentUiView && $rootScope.currentUiView.header === "EMPLOYER";
        //console.log("seek", isJobSeekerView);
        return !currentViewIsLogin;
      //return !currentViewIsLogin && employerHeaderNotShown && isJobSeekerView;

      case "home-url":
        isEmployer = $rootScope.userInfo && $rootScope.userInfo.roleName === "EMPLOYER";
        return isEmployer ? "hiring" : "home";
    }
  }

  $scope.logout = function () {
    securityService.logout();
    localStorageService.remove("social");
  }

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
  $scope.langKey = localStorage.NG_TRANSLATE_LANG_KEY;
  $scope.changeLanguages = function (key) {
    $scope.langKey = key;
    localStorage.NG_TRANSLATE_LANG_KEY = key;
    $window.location.reload();
  }
});
