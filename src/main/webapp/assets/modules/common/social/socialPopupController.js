techlooper.controller("socialPopupController", function($scope, apiService, localStorageService, $location) {
  $scope.loginBySocial = function (provider) {
    apiService.getSocialLoginUrl(provider).success(function (url) {
      //localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }
});