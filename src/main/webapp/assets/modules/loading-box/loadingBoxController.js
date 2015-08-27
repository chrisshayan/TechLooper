techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope, localStorageService, $location, utils) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });

  var joinNow = localStorageService.get("joinNow");
  if (joinNow == true) {
    var fromLastPrint = localStorageService.get("lastFoot");
    var roles = utils.getUiView(fromLastPrint).roles || [];// $rootScope.currentUiView.roles || [];
    if (roles.length == 0) {
      return $location.url(fromLastPrint);
    }
  }

});