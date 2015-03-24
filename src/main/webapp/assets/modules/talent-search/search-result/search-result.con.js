techlooper.controller("tsSearchResultController",
  function ($scope, $timeout, tsMainService, tsSearchResultService, $routeParams, $http, jsonValue, $location) {
    var request = {};
    var loopresPoint = 'Loopers Rank is a measure of profile completeness, relevance and experience; the more points means profile contains more personal information, person skills’ are more relevant to market demand and this person has contributed more to open source projects';
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

        if (this.talents.length / 20 === 10) {
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

    $scope.$watch("contentLoaded", function () {
      if ($scope.contentLoaded === true) {
        tsSearchResultService.updateSearchText(request);
        tsSearchResultService.init();
        $('[data-toggle="tooltip"]').tooltip({html:true});
      }
    });

    $scope.startHiring = tsMainService.searchTalent;

    if ($scope.search.talents.length === 0) {
      $scope.search.nextPage();
    }

    $scope.talentLink = function (talent) {
      return "#" + jsonValue.routerUris.talentProfile + "/" + $.base64.encode(talent.email);
    }

    $scope.handleLastItem = function() {
      $('[data-toggle="tooltip"]').tooltip({html:true});
    }
  });
