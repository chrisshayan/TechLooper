techlooper.directive('contestDetailScore', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/contestDetailScore.html",
    link: function (scope, element, attr, ctrl) {
      scope.saveScore = function() {
      //
      //  scope.registrant.saveCriteria();
      }
    }
  };
});