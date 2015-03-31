techlooper.controller("companyProfileController",
  function ($scope, companyProfileService) {
    $scope.follow = companyProfileService.followManager;
  });
