angular.module('Jobs').controller('searchFormController', ["$scope", "$location", function($scope, $location) {
  $scope.abcClick = function() {
    $location.path("/jobs/" + "acd");
  }
}]);