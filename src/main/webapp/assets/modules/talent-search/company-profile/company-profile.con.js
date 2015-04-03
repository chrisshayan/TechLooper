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

    data.totalViews = 0;
    data.totalApplications = 0;
    $.each(data.jobs, function(i, job) {
      data.totalViews += job.numOfViews;
      data.totalApplications += job.numOfApplications;
    });
    data.totalViews = data.totalViews.toLocaleString();
    data.totalApplications = data.totalApplications.toLocaleString();
    data.companySize = jsonValue.companySizes[data.companySizeId];
    $scope.companyInfo = data;
    console.log($scope.companyInfo);
  });
});
