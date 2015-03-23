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
      reachTop: false,
      total: 1,
      nextPage: function () {
        if (this.busy || this.talents.length === this.total) {
          return;
        }

        if (this.talents.length / 20 === 2) {
          this.busy = false;
          this.reachTop = true;
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
      tsSearchResultService.updateSearchText(request);
    }, 100);

    $scope.makeHover = function () {
      $timeout(function () {
        tsSearchResultService.init();
      });
    }
    $scope.startHiring = tsMainService.searchTalent;

    if ($scope.search.talents.length === 0) {
      $scope.search.nextPage();
    }

    $scope.showTalentDetails = function (talent) {
      $location.path(jsonValue.routes.talentProfile);
    }
  });
