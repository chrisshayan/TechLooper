techlooper.directive('contestDetailScore', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/contestDetailScore.html",
    link: function (scope, element, attr, ctrl) {
      scope.saveScore = function() {
        scope.registrant.validate();
        if (scope.registrant.$invalid) {
          return;
        }

        scope.registrant.saveCriteria();
        scope.registrant.hide()
      }

      scope.hideCriteriaForm = function() {
        scope.registrant.hide();
        scope.registrant.refreshCriteria();
      }
    }
  };
});