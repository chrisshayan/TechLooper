techlooper.controller("homeController", function ($scope, securityService, apiService, localStorageService, $location,
                                                  jsonValue, utils, $timeout, $rootScope) {

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

  //scope.status = function (type) {
  //  switch (type) {
  //    case "show-error":
  //      var errorType = arguments[1];
  //      return scope.jobAlert.tag.$error[errorType] || scope.jobAlert.autoTag.$error[errorType];
  //
  //    //case "show-errors":
  //    //  return scope.tagForm.$submitted || scope.tagForm.tag.$edited || scope.tagForm.autoTag.$edited;
  //  }

  //  return false;
  //}
});