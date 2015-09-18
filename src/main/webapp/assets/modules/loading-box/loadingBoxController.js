techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location, securityService) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    //var fromLastPrint = localStorageService.get("lastFoot");
    //var ui = utils.getUiView(fromLastPrint);
    //var roles = ui.roles || [];// $rootScope.currentUiView.roles || [];
    //if (roles.length == 0) {
    //  return securityService.routeByRole();
    //}

    return securityService.routeByRole();
    //var fromLastPrint = localStorageService.get("lastFoot");
    //var ui = utils.getUiView(fromLastPrint);
    //if (ui.ignoreIfLastFoot) {
    //  return securityService.routeByRole();
    //}
    //var roles = ui.roles || [];// $rootScope.currentUiView.roles || [];
    //if (roles.length == 0) {
    //  return $location.url(fromLastPrint);
    //}
  }

});