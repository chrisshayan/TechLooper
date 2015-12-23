techlooper.directive('challengeList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/challengeList.html",
    link: function (scope, element, attr, ctrl) {
      scope.$internalForm = {visible: false};

      scope.toggleJoinInternalForm = function () {
        scope.$internalForm.visible = !scope.$internalForm.visible;
      }
    }
  };
})