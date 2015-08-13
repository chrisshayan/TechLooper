techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  apiService.getEmployerDashboardInfo()
    .success(function (data) {
      $scope.dashboardInfo = data;
        console.log($scope.dashboardInfo);
      })
    .finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});

});
