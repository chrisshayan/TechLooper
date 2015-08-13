techlooper.controller("socialPopupController", function($scope, apiService, securityService) {
  $scope.loginBySocial = function (provider) {
    securityService.removeProtectedLastFoot();
    apiService.getSocialLoginUrl(provider).success(function (url) {
      //localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }
});