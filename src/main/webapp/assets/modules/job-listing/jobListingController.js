techlooper.controller("jobListingController", function (apiService, $scope, vnwConfigService, $routeParams) {

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
    var location = searchParams.length > 1 ? searchParams[1] : "";
    var page = searchParams.length > 2 ? searchParams[2] : 0;

    apiService.filterJob(keyword, location, page).success(function(response) {
      $scope.totalPage = response.totalPage;
      $scope.totalJob = response.totalJob;
      $scope.page = response.page;
      $scope.jobs = response.jobs;
    });
  }

  $scope.getPageRange = function(totalPage, currentPage) {
    var numberOfShownPages = 5;
    var median = Math.floor(numberOfShownPages / 2) + 1;

    var start = 1;
    if (currentPage > median) {
      start = currentPage - median + 1;
    }


    var end = totalPage;
    if (currentPage < totalPage - 3 && start != 1) {
      end = currentPage + median - 1;
    } else {
      end = numberOfShownPages;
    }

    var list = [];
    for (var i = start; i <= end; i++) {
      list.push(i);
    }
    return list;
  }

  $scope.filterJob = function() {
    var keyword = $scope.searchJob.keyword;
    var location = vnwConfigService.getLocationText($scope.searchJob.locationId, "en");
    var page = $scope.page;
    apiService.filterJob(keyword, location, page).success(function(response) {
      $scope.totalPage = response.totalPage;
      $scope.totalJob = response.totalJob;
      $scope.page = response.page;
      $scope.jobs = response.jobs;
    });
  }
});