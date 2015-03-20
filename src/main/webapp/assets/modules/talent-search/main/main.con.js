techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  $timeout(function () {
    tsMainService.enableSelectOptions();
    tsMainService.validationFeedback();
    tsMainService.scrollToReason();
  }, 500);

  $scope.startHiring = tsMainService.searchTalent;
});
