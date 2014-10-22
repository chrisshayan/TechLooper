angular.module('Jobs').controller('searchResultController', ["$scope", "$routeParams", function($scope, $routeParams) {
  console.log($routeParams.text);
}]);