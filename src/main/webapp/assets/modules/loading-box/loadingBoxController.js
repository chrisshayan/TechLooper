techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    var fromLastPrint = localStorageService.get("lastFoot");
    localStorageService.remove("lastFoot");
    return $location.url(fromLastPrint);
  }
});