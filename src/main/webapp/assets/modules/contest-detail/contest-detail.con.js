techlooper.controller('contestDetailController', function ($scope) {
  $scope.countDownDay = parseInt(moment().countdown("07/10/2015", countdown.DAYS, NaN, 2).toString());
});
