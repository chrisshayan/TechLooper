techlooper.controller("jobListingController", function (apiService, $scope, vnwConfigService) {

  $scope.locationsConfig = vnwConfigService.locationsSearchSelectize;

  apiService.listJob().success(function(response) {
    $scope.totalPage = response.totalPage;
    $scope.totalJob = response.totalJob;
    $scope.page = response.page;
    $scope.jobs = response.jobs;
  });

  $scope.getPageRange = function(totalPage, currentPage) {
    var numberOfShownPages = 5;
    var median = numberOfShownPages / 2 + 1;

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