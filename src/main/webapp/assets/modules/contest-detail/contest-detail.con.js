techlooper.controller('contestDetailController', function ($scope) {
  $scope.showDeadlineInfo = function(){
    $scope.toggle = !$scope.toggle;
  }
});