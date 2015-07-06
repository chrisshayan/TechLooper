techlooper.controller('contestsController', function (apiService) {
  apiService.searchContests().success(function(contests) {
    console.log(contests);
  });
});