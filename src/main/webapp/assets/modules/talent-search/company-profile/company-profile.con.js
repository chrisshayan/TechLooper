techlooper.controller("companyProfileController", function ($scope, companyProfileService, $routeParams, $http, jsonValue) {
  $scope.follow = companyProfileService.followManager;

  var companyName = $routeParams.companyName;
  $http.get(jsonValue.httpUri.company + "/" + companyName).success(function(data) {
    $.each(data.benefits, function(i, benefit) {
      benefit.name = jsonValue.benefits[benefit.benefitId].name;
      benefit.iconName = jsonValue.benefits[benefit.benefitId].iconName;
    });
    $.each(data.industries, function(i, industry) {
      industry.value = jsonValue.industries[industry.industryId].value;
    });
    data.companySize = jsonValue.companySizes[data.companySizeId];
    $scope.companyInfo = data;
    console.log($scope.companyInfo);
  });
});
