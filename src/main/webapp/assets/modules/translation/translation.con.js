angular.module('Common').controller('translationController', function ($scope, $translate, utils, jsonValue) {
  $(".langKey").click(function () {
    $translate.use($translate.use() === "vi" ? "en" : "vi");
    $scope.$apply();
    utils.sendNotification(jsonValue.notifications.changeLang, $translate.use());
  });
});
