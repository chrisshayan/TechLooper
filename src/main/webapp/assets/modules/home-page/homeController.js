techlooper.controller("homeController", function($scope, securityService, apiService, localStorageService, $location) {

  $scope.loginByFacebook = function() {
    apiService.getSocialLoginUrl("FACEBOOK").success(function(url) {
      localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }

  $scope.loginByGoogle = function() {
    apiService.getSocialLoginUrl("GOOGLE").success(function(url) {
      localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }

  var code = localStorageService.get("code");
  if (code) {
    securityService.login(code, localStorageService.get("social"));
    localStorageService.remove("code", "social");
  }


});