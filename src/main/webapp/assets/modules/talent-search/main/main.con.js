techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  //$timeout(function () {
  //  tsMainService.enableSelectOptions();
  //  tsMainService.validationFeedback();
  //  tsMainService.scrollToReason();
  //}, 1000);

  //tsMainService.enableSelectOptions();
  //tsMainService.validationFeedback();
  //tsMainService.scrollToReason();

  $scope.$watch("contentLoaded", function() {
    if ($scope.contentLoaded === true) {
      tsMainService.scrollToReason();
    }
  });

  $scope.startHiring = tsMainService.searchTalent;
});
