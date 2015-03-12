techlooper.controller("tsSearchResultController", function ($scope, $timeout, tsMainService) {
  $timeout(function () {
    tsMainService.enableSelectOptions();
  }, 100);
});
