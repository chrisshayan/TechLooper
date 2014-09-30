angular.module('Common').controller('translationController', function($scope, $translate, $element) {
   $element.click(function() {
      $translate.use($translate.use() == "vi" ? "en-US" : "vi");
      $scope.$apply();
   });
});