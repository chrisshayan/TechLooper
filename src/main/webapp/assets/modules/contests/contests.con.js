techlooper.controller('contestsController', function (apiService, $scope) {
  apiService.searchContests().success(function(contests) {
    $scope.contestsList = contests;
  });
});