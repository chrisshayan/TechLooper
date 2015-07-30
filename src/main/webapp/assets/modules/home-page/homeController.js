techlooper.controller("homeController", function ($scope, securityService, apiService, localStorageService, $location,
                                                  jsonValue, $filter, $timeout) {

  $scope.today = moment().format(jsonValue.dateFormat);

  $scope.loginBySocial = function (provider) {
    apiService.getSocialLoginUrl(provider).success(function (url) {
      localStorageService.set("lastFoot", $location.url());
      window.location = url;
    });
  }

  var code = localStorageService.get("code");
  if (code) {
    securityService.login(code, localStorageService.get("social"), "social")
      .success(function (data) {
        securityService.getCurrentUser("social")
          .success(function (data) {

          });
      })
      .error(function (data) {
      });
    localStorageService.remove("code", "social");
  }

  apiService.getPersonalHomepage().success(function (data) {
    $scope.homePage = data;
    $scope.homePage.termStatistic.logo = "images/" + $.grep(jsonValue.technicalSkill, function (skill) {
        return skill.term == $scope.homePage.termStatistic.term;
      })[0].logo;
  });

  //TODO remove timeout function
  $timeout(function () {
    var tallest = 0;
    $('.personal-site-content-detail').find('.box-content').each(function () {
      var thisHeight = $(this).height();
      if (thisHeight > tallest)
        tallest = thisHeight;
    });
    $('.personal-site-content-detail').find('.box-content').height(tallest + $('.cta-button').height());
  }, 1400);
});