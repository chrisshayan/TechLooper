techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    $timeout(function () {
      tsMainService.enableSelectOptions();
      tsSearchResultService.init();
    }, 100);

    try {
      var request = JSON.parse($routeParams.text);
      $timeout(function () {
        console.log(tsMainService.getSkills());
        tsMainService.getSkills().setValue(request.skills);
        console.log(tsMainService.getLocations());
        console.log(tsMainService.getTitles());
        console.log(tsMainService.getCompanies());
      }, 100);
      $http.post(jsonValue.httpUri.searchTalent, JSON.stringify(request))
        .success(function (data, status, headers, config) {
          $scope.talents = data;
        });
    }
    catch(e){
      $location.path("/");
    }
  });
