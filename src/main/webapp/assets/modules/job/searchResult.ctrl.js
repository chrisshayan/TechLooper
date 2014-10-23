angular.module('Jobs', ['infinite-scroll']).controller('searchResultController',
  ["$scope", "$routeParams", "connectionFactory", "jsonValue",
    function ($scope, $routeParams, connectionFactory, jsonValue) {
      connectionFactory.initialize($scope);
      $scope.search = {
        jobs: [],
        totalPages : 0,
        pageNumber : 1,
        busy: false,
        nextPage: function () {
          if (this.busy) return;
          this.busy = true;
          if ($scope.search.pageNumber == 1 || $scope.search.pageNumber <= $scope.search.totalPages) {
              connectionFactory.findJobs({"terms": $routeParams.text, "pageNumber": $scope.search.pageNumber});
              console.log("scrolling me : " + $routeParams.text + $scope.search.pageNumber);
          } else {
              $scope.search.busy = false;
              $scope.$apply();
          }
        }
      };


      $scope.$on(jsonValue.events.foundJobs, function (event, data) {
          // count total pages at the first time
          if ($scope.search.pageNumber == 1) {
              console.log("Total : " + data.total);
              console.log("Item/Page : " + data.jobs.length);
              //TODO : move this total page calculation to util
              if (data.total % data.jobs.length == 0) {
                  $scope.search.totalPages = Math.floor(data.total / data.jobs.length);
              } else {
                  $scope.search.totalPages = Math.floor(data.total / data.jobs.length) + 1;
              }
          }
        $scope.search.jobs = $scope.search.jobs.concat(data.jobs);
        $scope.search.busy = false;
        $scope.search.pageNumber++;
        $scope.$apply();
      });
    }]);

