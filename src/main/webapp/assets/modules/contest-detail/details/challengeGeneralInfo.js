techlooper.directive('challengeGeneralInfo', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/details/challengeGeneralInfo.html",
    link: function (scope, element, attr, ctrl) {
      scope.$internalForm = {visible: false};

      scope.toggleJoinInternalForm = function () {
        scope.$internalForm.visible = !scope.$internalForm.visible;
      }
    }
  };
});