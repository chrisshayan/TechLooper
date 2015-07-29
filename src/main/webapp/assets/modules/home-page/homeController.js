techlooper.controller("homeController", function($scope, securityService, apiService, localStorageService, $location,
                                                 jsonValue, $filter) {

  $scope.today = moment().format(jsonValue.dateFormat);

  $scope.loginBySocial = function(provider) {
    apiService.getSocialLoginUrl(provider).success(function(url) {
      localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }

  var code = localStorageService.get("code");
  if (code) {
    securityService.login(code, localStorageService.get("social"), "social");
    localStorageService.remove("code", "social");
  }

  apiService.getPersonalHomepage().success(function(data) {
    $scope.homePage = data;
    $scope.homePage.termStatistic.logo = "images/" +  $.grep(jsonValue.technicalSkill, function(skill) {
      return skill.term == $scope.homePage.termStatistic.term;
    })[0].logo;

    //$filter("progress")($scope.homePage.latestChallenge, "challenge");

    console.log($scope.homePage);
  });
});