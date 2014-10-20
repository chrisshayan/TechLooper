angular.module('Common').controller('translationController', ["$scope", "$translate", "jsonValue", function ($scope, $translate, jsonValue) {
  $(".langKey").click(function () {
    $translate.use($translate.use() == "vi" ? "en" : "vi");
    $scope.$apply();
  });
}]);
