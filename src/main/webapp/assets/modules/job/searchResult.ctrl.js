angular.module('Jobs', ['infinite-scroll']).controller('searchResultController',
  ["$scope", "$routeParams", "connectionFactory", "jsonValue",
    function ($scope, $routeParams, connectionFactory, jsonValue) {
      //console.log($routeParams.text);
      connectionFactory.initialize($scope);
      $scope.search = {
        jobs: [],
        busy: false,
        nextPage: function () {
          if (this.busy) return;
          this.busy = true;
          console.log("scrolling me");
          //for (var i = 0; i < 20; i++) {
          //  $scope.search.jobs.push({title: "sample job " + i});
          //}
          connectionFactory.findJobs({"terms": "java", "pageNumber": "1"});
        }
      };


      $scope.$on(jsonValue.events.foundJobs, function (event, data) {
        $scope.search.jobs = $scope.search.jobs.concat(data.jobs);
        $scope.search.busy = false;
        $scope.$apply();
      });
    }]);

