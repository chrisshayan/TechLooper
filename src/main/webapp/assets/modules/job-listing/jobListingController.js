techlooper.controller("jobListingController", function (apiService, $scope, vnwConfigService, $routeParams, $location, utils) {

  $scope.locationsConfig = vnwConfigService.locationsSearchSelectize;

  var searchText = $routeParams.searchText;
  if (!searchText) {
    apiService.listAllJobs().success(function(response) {
      $scope.totalPage = response.totalPage;
      $scope.totalJob = response.totalJob;
      $scope.page = response.page;
      $scope.jobs = response.jobs;
    });
  } else {
    var searchParams = searchText.split("+", 3);
    var keyword = searchParams.length > 0 ? searchParams[0] : "";
    var locationId = searchParams.length > 1 ? searchParams[1] : "";
    var page = $routeParams.page ? $routeParams.page : 1;

    var location = "";
    if (locationId) {
      location = utils.toAscii(vnwConfigService.getLocationText(locationId));
    }

    apiService.filterJob(keyword, location, page).success(function(response) {
      $scope.totalPage = response.totalPage;
      $scope.totalJob = response.totalJob;
      $scope.page = response.page;
      $scope.jobs = response.jobs;
    });

    $scope.searchJob = {keyword : keyword, locationId : locationId, location : location};
  }

  $scope.getPageRange = function() {
    var numberOfShownPages = 5;
    var start = 1;
    if ($scope.page && $scope.totalPage > numberOfShownPages) {
      start = $scope.page;
    }
    var end = start + numberOfShownPages - 1;
    if (end > $scope.totalPage) {
      end = $scope.totalPage;
    }

    var list = [];
    for (var i = start; i <= end; i++) {
      list.push(i);
    }
    return list;
  }

  $scope.filterJob = function() {
    var keyword = $scope.searchJob.keyword;
    var locationId = $scope.searchJob.locationId;
    var location = vnwConfigService.getLocationText(locationId, "en");
    $location.path("/job-listing/" + utils.toAscii(keyword) + "+" + locationId + "+" + utils.toAscii(location) + "/1");
  }
});