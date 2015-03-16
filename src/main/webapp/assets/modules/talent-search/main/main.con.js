techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  $timeout(function () {
    tsMainService.applySlider();
    tsMainService.location();
    tsMainService.enableSelectOptions();
    tsMainService.validationFeedback();
    tsMainService.countdown();
  }, 500);

  $scope.startHiring = tsMainService.searchTalent;
});
