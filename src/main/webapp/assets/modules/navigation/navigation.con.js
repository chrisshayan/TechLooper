techlooper.controller("navigationController", function ($scope, securityService, apiService, localStorageService, $location,
                                                  jsonValue, utils, $timeout, $rootScope) {

  $scope.state = function(type) {
    switch (type) {
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
            case jsonValue.views.freelancerProjects:
            case jsonValue.views.howItWorks:
            case jsonValue.views.analyticsSkill:
              return true;
          }
          return false;
        }
        return $rootScope.userInfo.roleName === "JOB_SEEKER";
    }
  }

  $scope.logout = function() {
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

  //$scope.loginBySocial = function (provider) {
  //  apiService.getSocialLoginUrl(provider).success(function (url) {
  //    localStorageService.set("lastFoot", $location.url());
  //    window.location = url;
  //  });
  //}

});
