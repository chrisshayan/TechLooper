angular.module('Common').controller('translationController', function ($scope, $translate, jsonValue) {
  $(".langKey").click(function () {
    $translate.use($translate.use() == "vi" ? "en" : "vi");
    $scope.$apply();
  });
});
