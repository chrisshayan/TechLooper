techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location, securityService) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    return securityService.routeByRole();
  }

  //var param = $location.search();
  //if (!$.isEmptyObject(param)) {
  //  switch (param.action) {
  //    case "challenge-detail":
  //      localStorageService.set("challengePhaseName", param.name);
  //      $location.url("/home");
  //      break;
  //  }
  //}

  var param = $location.search();
  if ($.isEmptyObject(param)) {
    $location.url("/home");
  }
});