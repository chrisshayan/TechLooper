techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location, securityService) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    return securityService.routeByRole();
  }

  var param = $location.search();
  if (!$.isEmptyObject(param)) {
    switch (param.action) {
      case "registerVnwUser":
        localStorageService.set("lastName", param.lastName);
        localStorageService.set("firstName", param.firstName);
        localStorageService.set("email", param.email);
        break;

      case "loginBySocial":
        securityService.login(param.code, param.social, param.social);
        break;

      case "redirectJA":
        window.location.href = param.targetUrl;
        break;

      case "cancel":
        $location.url("/");
        break;
    }
  }

  $location.url("/");
});