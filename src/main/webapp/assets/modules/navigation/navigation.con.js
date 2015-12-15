techlooper.controller("navigationController", function ($scope, securityService, apiService, localStorageService, $location,
                                                        jsonValue, utils, $timeout, $rootScope, $window) {
  $scope.state = function (type) {
    var isEmployer = undefined;
    switch (type) {
      case "show-employer-header":
        var isEmployerView = $rootScope.currentUiView && $rootScope.currentUiView.header === "EMPLOYER";
        isEmployer = $rootScope.userInfo && $rootScope.userInfo.roleName === "EMPLOYER";
        return isEmployerView || isEmployer;

      case "show-job-seeker-header":
        var employerHeaderNotShown = !$scope.state("show-employer-header");
        var isJobSeekerView = $rootScope.currentUiView && $rootScope.currentUiView.type != "LOGIN";
        return employerHeaderNotShown && isJobSeekerView;

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
  $scope.changeLanguages = function(key){
    $scope.langKey = key;
    localStorage.NG_TRANSLATE_LANG_KEY = key;
    $window.location.reload();
  }
  //$rootScope.$watch(function () {
  //  var pathURL = $location.path();
  //  var detailsPage = -1;
  //  if(pathURL.indexOf('challenge-detail') > -1 || pathURL.indexOf('project-detail') > -1){
  //    detailsPage = 1;
  //  }
  //  if(detailsPage == 1){
  //    $('.homepage-link').addClass('detail-page');
  //  }else{
  //    $('.homepage-link').removeClass('detail-page');
  //  }
  //});
});
