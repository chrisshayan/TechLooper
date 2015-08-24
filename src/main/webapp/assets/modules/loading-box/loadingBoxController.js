techlooper.controller("loadingBoxController", function (utils, jsonValue, $scope) {
  utils.sendNotification(jsonValue.notifications.loading, $(window).height());

  $scope.$on('$destroy', function () {
    utils.sendNotification(jsonValue.notifications.loaded);
  });
});