angular.module('Common').controller('translationController', ["$scope", "$translate", function ($scope, $translate) {
  $(".langKey").click(function () {
    $translate.use($translate.use() == "vi" ? "en-US" : "vi");
    $scope.$apply();
  });
}]);
