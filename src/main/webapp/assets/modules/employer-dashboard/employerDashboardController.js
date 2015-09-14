techlooper.controller('employerDashboardController', function ($scope, jsonValue, utils, apiService) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  apiService.getEmployerDashboardInfo()
    .success(function (data) {
      data.challenges.sort(function (left, right) {
        var rightStartDate = moment(right.startDateTime, jsonValue.dateFormat);
        var leftStartDate = moment(left.startDateTime, jsonValue.dateFormat);
        return rightStartDate.isAfter(leftStartDate);
      });
      data.projects.sort(function (left, right) {
        return right.projectId - left.projectId;
      });
      $scope.dashboardInfo = data;
    })
    .finally(function () {utils.sendNotification(jsonValue.notifications.loaded, $(window).height());});

  $scope.myFunction = function(val) {
    return val.progress.translate != 'notStart' && val.progress.translate != 'closed';
  };
});
