techlooper.controller("socialPopupController", function($scope, apiService, securityService) {
  $scope.loginBySocial = function (provider) {
    //securityService.removeProtectedLastFoot();
    apiService.getSocialLoginUrl(provider).success(function (url) {
      //localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }
  $scope.closeSignIn = function(){
    $('#signIn').modal('hide');
    window.location.href = "#/user-type";
  }
});