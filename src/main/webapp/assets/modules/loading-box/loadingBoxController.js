techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location, securityService) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    return securityService.routeByRole();
  }

  $location.url("/");
});