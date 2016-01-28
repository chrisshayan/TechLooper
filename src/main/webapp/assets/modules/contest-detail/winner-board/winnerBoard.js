techlooper.directive('winnerBoard', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/winner-board/winnerBoard.html",
      link: function (scope, element, attr, ctrl) {
        //scope.reloadWinners = function() {
        //  reloadWinners
        //}
      }
    };
  });