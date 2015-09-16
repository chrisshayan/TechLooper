techlooper.controller("jobListingController", function (apiService, $scope, vnwConfigService, $routeParams, $location, utils, jsonValue, $timeout ) {

  $scope.locationsConfig = vnwConfigService.locationsSelectize;

  var searchText = $routeParams.searchText;

  var capitalizeWords = function capitalizeWords(str) {
    if (str) {
      str = str.replace(/-/g, ' ');
      return str.replace(/\w\S*/g, function(txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
      });
    }
    return "";
  }

  var bindSearchResultData = function bindSearchResultData(response) {
    $scope.totalPage = response.totalPage;
    $scope.totalJob = response.totalJob;
    $scope.page = response.page;
    $scope.jobs = response.jobs;
    $.each($scope.jobs, function (i, job) {
      job.crawlSource = jsonValue.crawlSourcesMap[job.crawlSource] ? jsonValue.crawlSourcesMap[job.crawlSource].name : "Internet";
      var locations = job.location.split(",");
      $.each(locations, function(j, id){
        if($.isNumeric(id)){
          if(j == 0){
            job.locationText = vnwConfigService.getLocationText(id);
          }else{
            job.locationText = job.locationText + ', ' + vnwConfigService.getLocationText(id);
          }
        }else{
          if(j == 0){
            job.locationText = id;
          }else{
            job.locationText = job.locationText + ', ' + job.locationText+ id;
          }
        }
      });
    });
  }

  if (!searchText) {
    apiService.listAllJobs().success(function(response) {
      utils.sendNotification(jsonValue.notifications.loading);
      bindSearchResultData(response);
    }).finally(function () {
      utils.sendNotification(jsonValue.notifications.loaded);
    });
  } else {
    var searchParams = searchText.split("+");
    var keyword = searchParams.length > 0 ? capitalizeWords(searchParams[0]) : "";
    var locationId = searchParams.length > 1 ? searchParams[1] : "";
    var page = $routeParams.page ? $routeParams.page : 1;

    var location = "";
    if (locationId && locationId !== "0") {
      location = utils.toAscii(vnwConfigService.getLocationText(locationId, "en"));
    }

    apiService.filterJob(keyword, location, page).success(function(response) {
      bindSearchResultData(response);
    });

    $scope.searchJob = {keyword : keyword, locationId : locationId, location : location};
  }

  $scope.getPageRange = function() {
    var numberOfShownPages = 5;

    var start = 1;
    var end = numberOfShownPages;
    if ($scope.totalPage > numberOfShownPages) {
      var median = numberOfShownPages % 2 == 0 ? numberOfShownPages / 2 : Math.floor(numberOfShownPages / 2) + 1;
      var distance = median - 1;
      if ($scope.page > median) {
        if ($scope.totalPage - $scope.page < distance) {
          start = $scope.page - (distance + (numberOfShownPages - ($scope.totalPage - $scope.page) - median));
        } else {
          start = $scope.page - distance;
        }
        if ($scope.page + distance <= $scope.totalPage) {
          end = $scope.page + distance;
        } else {
          end = $scope.totalPage;
        }
      } else {
        start = 1;
        end = numberOfShownPages;
      }
    } else {
      start = 1;
      end = $scope.totalPage;
    }

    var list = [];
    for (var i = start; i <= end; i++) {
      list.push(i);
    }

    $scope.previousPage = {
      isEnable : false,
      pageIndex : 1
    };

    $scope.nextPage = {
      isEnable : false,
      pageIndex : 1
    };

    if (start > 1 || (start == 1 && $scope.page > 1)) {
      $scope.previousPage.isEnable = true;
      $scope.previousPage.pageIndex = $scope.page - 1;
    }

    if ($scope.page < $scope.totalPage) {
      $scope.nextPage.isEnable = true;
      $scope.nextPage.pageIndex = $scope.page + 1;
    }

    return list;
  }

  $scope.filterJob = function() {
    var keyword = $scope.searchJob.keyword ? $scope.searchJob.keyword : "";
    var locationId = $scope.searchJob.locationId && $scope.searchJob.locationId !== "0" ? $scope.searchJob.locationId : "";
    var location = "";
    if (locationId) {
      location = vnwConfigService.getLocationText(locationId, "en");
    }
    var page = 1;

    apiService.filterJob(keyword, location, page).success(function(response) {
      bindSearchResultData(response);
    });

    //var searchPath = $scope.buildSearchPath(keyword, locationId, location, page);
    //$location.path(searchPath);
  }

  $scope.buildSearchPath = function(keyword, locationId, location, page) {
    var result = "/job-listing/";
    if (keyword) {
      result += utils.toAscii(keyword);
    }
    if (locationId) {
      result += "+" + locationId;
    }
    if (location) {
      result += "+" + utils.toAscii(location);
    }
    if (page && page > 1) {
      result += "/" + page;
    }
    return result;
  }


  $timeout(function () {
    $('p.offers').on("click", function(){
      $(this).next().toggleClass( "show", 1000);
    });
  }, 1000);
});