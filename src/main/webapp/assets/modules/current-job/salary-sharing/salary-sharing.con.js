techlooper.controller("salarySharingController", function ($scope, salarySharingService) {
  salarySharingService.init();
  $scope.createReport = function () {
    salarySharingService.validationForm();
  }
});
