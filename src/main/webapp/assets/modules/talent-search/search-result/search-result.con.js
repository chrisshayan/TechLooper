techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    $timeout(function () {
      tsMainService.enableSelectOptions();

      try {
        var request = CryptoJS.enc.Base64.parse($routeParams.text).toString(CryptoJS.enc.Utf8);//JSON.parse($routeParams.text);
        request = JSON.parse(request);
        tsSearchResultService.updateSearchText(request);
        $http.post(jsonValue.httpUri.searchTalent, JSON.stringify(request))
          .success(function (data, status, headers, config) {
            $scope.talents = data;
          });
      }
      catch(e){
        $location.path("/");
      }
    }, 200);

    $scope.doSomething = function() {
      $timeout(function () {
        tsSearchResultService.init();
      }, 100);
    }
    $scope.startHiring = tsMainService.searchTalent;
  });
