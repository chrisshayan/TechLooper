techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    $timeout(function () {
      tsMainService.enableSelectOptions();
      tsSearchResultService.init();
    }, 100);

    try {
      var request = JSON.parse($routeParams.text);
      $http.post(jsonValue.httpUri.searchTalent, JSON.stringify(request))
        .success(function (data, status, headers, config) {
          console.log(data);
          $scope.talents = data;
        });
    }
    catch(e){
      $location.path("/");
    }
  });
