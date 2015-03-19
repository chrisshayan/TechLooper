techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    var request = {};
    $.each($routeParams.text.split("::"), function (i, q) {
      if (q.length > 0) {
        var qs = q.split(":");
        request[qs[0]] = qs[1].split(",");
      }
    });

    $scope.search = {
      talents: [],
      pageIndex: -1,
      busy: false,
      total: 1,
      nextPage: function () {
        if (this.busy || this.talents.length === this.total) {
          return;
        }

        if (this.talents.length / 20 === 10) {
          this.busy = false;
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

    $scope.makeHover = function () {
      tsMainService.enableSelectOptions();
      tsSearchResultService.updateSearchText(request);
      $timeout(function() {
        tsSearchResultService.init();
      });
    }
    $scope.startHiring = tsMainService.searchTalent;

    if ($scope.search.talents.length === 0) {
      $scope.search.nextPage();
    }
  });
