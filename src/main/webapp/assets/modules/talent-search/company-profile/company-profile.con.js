techlooper.controller("companyProfileController", function ($scope, companyProfileService, $routeParams, $http, jsonValue) {
  $scope.follow = companyProfileService.followManager;

  var companyName = $routeParams.companyName;
  $http.get(jsonValue.httpUri.company + "/" + companyName).success(function(data) {
    $scope.companyInfo = data;
    console.log(data);
  });
});
