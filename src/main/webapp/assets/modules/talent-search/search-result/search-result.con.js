techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    try {
      var request = CryptoJS.enc.Base64.parse($routeParams.text).toString(CryptoJS.enc.Utf8);
      request = JSON.parse(request);
    }
    catch(e){
      $location.path("/");
    }

    $scope.search = {
      talents: [],
      pageIndex: -1,
      busy: false,
      total: 1,
      nextPage: function () {
        if (this.busy || this.talents.length === this.total) {
          return;
        }
        this.busy = true;
        ++this.pageIndex;
        request.pageIndex = this.pageIndex;
        $http.post(jsonValue.httpUri.searchTalent, JSON.stringify(request))
          .success(function (data, status, headers, config) {
            $scope.search.talents = $scope.search.talents.concat(data.result);
            $scope.search.total = data.total;
            $scope.search.busy = false;
          });
      }
    }

    $timeout(function () {
      tsMainService.enableSelectOptions();

      try {
        var request = CryptoJS.enc.Base64.parse($routeParams.text).toString(CryptoJS.enc.Utf8);
        request = JSON.parse(request);
        tsSearchResultService.updateSearchText(request);
      }
      catch(e){
        $location.path("/");
      }
    }, 200);

    $scope.makeHover = function() {
      $timeout(function () {
        tsSearchResultService.init();
      }, 100);
    }
    $scope.startHiring = tsMainService.searchTalent;
  });
