angular.module("Navigation").controller("navigationController", function ($scope, utils, jsonValue, navigationService) {
  navigationService.initialize();
  //navigationService.restartTour();
  //$scope.changeChart = function (event) {
  //  if ($(event.currentTarget).hasClass('m-pie-chart')) {
  //    $(event.currentTarget).attr('href', '#/bubble-chart').removeClass('m-pie-chart');
  //  }
  //  else {
  //    $(event.currentTarget).attr('href', '#/pie-chart').addClass('m-pie-chart');
  //  }
  //}
});
