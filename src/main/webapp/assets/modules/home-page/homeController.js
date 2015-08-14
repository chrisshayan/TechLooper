techlooper.controller("homeController", function ($scope, securityService, apiService, localStorageService, $location,
                                                  jsonValue, utils, $timeout, vnwConfigService, $translate) {

  apiService.getPersonalHomepage().success(function (data) {
    $scope.homePage = data;
    $scope.homePage.termStatistic.logo = "images/" + $.grep(jsonValue.technicalSkill, function (skill) {
        return skill.term == $scope.homePage.termStatistic.term;
      })[0].logo;
  });

  //TODO remove timeout function
  $timeout(function () {
    var tallest = 0;
    $('.box-container-block').find('.box-content').each(function () {
      var thisHeight = $(this).height();
      if (thisHeight > tallest)
        tallest = thisHeight;
    });
    $('.box-container-block').find('.box-content').height(tallest + $('.cta-button').height());
  }, 1400);

  $scope.locationsConfig = vnwConfigService.locationsSelectize;

  $scope.createJobAlert = function () {
    $scope.jobAlertForm.$setSubmitted();
    if ($scope.jobAlertForm.$invalid) {
      return;
    }

    apiService.createTechlooperJobAlert($scope.jobAlert.email, $scope.jobAlert.keyword,
      vnwConfigService.getLocationText($scope.jobAlert.locationId, "en"), $translate.use())
      .success(function (data) {
          $scope.sendMailSuccessfulMessage = true;
          $scope.jobAlert.email = '';
      });
  }
});