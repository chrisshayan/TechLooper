techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  apiService.getEmployerDashboardInfo()
    .success(function (data) {
      console.log(data);
      $scope.dashboardInfo = data;
    })
    .finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});
});
