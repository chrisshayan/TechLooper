techlooper.controller("tsSearchResultController", function ($scope, $timeout, tsMainService, tsSearchResultService) {
  $timeout(function () {
    tsMainService.enableSelectOptions();
    tsSearchResultService.init();
  }, 100);
});
