techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  $timeout(function () {
    tsMainService.enableSelectOptions();
    tsMainService.validationFeedback();
    tsMainService.scrollToReason();
  }, 1000);

  $scope.startHiring = tsMainService.searchTalent;
});
