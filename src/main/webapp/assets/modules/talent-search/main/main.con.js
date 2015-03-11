techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  $timeout(tsMainService.applySlider, 100);
  $timeout(tsMainService.location(), 100);
});
