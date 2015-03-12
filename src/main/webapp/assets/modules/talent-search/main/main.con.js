techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  $timeout(function () {
    tsMainService.applySlider();
    tsMainService.location();
    tsMainService.enableSelectOptions();
  }, 100);

  $scope.startHiring = tsMainService.searchTalent;
});
