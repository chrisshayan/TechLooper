techlooper.controller("tsMainController", function ($scope, $timeout, tsMainService) {
  $timeout(function() {
    tsMainService.applySlider();
    tsMainService.location();
  }, 100);
});
