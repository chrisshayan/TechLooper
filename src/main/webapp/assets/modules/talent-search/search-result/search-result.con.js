techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    $timeout(function () {
      tsMainService.enableSelectOptions();
      tsSearchResultService.init();

      try {
        var request = JSON.parse($routeParams.text);
        tsSearchResultService.updateSearchText(request);
        $http.post(jsonValue.httpUri.searchTalent, JSON.stringify(request))
          .success(function (data, status, headers, config) {
            $scope.talents = data;
          });
      }
      catch(e){
        console.log(e);
        $location.path("/");
      }
    }, 100);
  });
