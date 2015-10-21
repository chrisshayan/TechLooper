techlooper.directive('contestDetailScore', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/contestDetailScore.html",
    scope: {
      registrant: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.saveScore = function() {

        scope.registrant.saveCriteria();
      }
    }
  };
});