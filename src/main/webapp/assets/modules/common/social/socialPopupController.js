techlooper.controller("socialPopupController", function($scope, apiService, $translate) {
  $scope.loginBySocial = function (provider) {
    //securityService.removeProtectedLastFoot();
    apiService.getSocialLoginUrl(provider).success(function (url) {
      //localStorageService.set("lastFoot", $location.url());
      window.location = url + "&state=" + $translate.use();
    });
  }
  $scope.closeSignIn = function(){
    $('#signIn').modal('hide');
    window.location.href = "#/user-type";
  }
});