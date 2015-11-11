techlooper.directive('funnelManagement', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants/funnelManagement.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('registrationList', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants/registrationList.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
.directive('qualifyAllToNextPhase', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/registrants/qualifyAllToNextPhase.html",
    link: function (scope, element, attr, ctrl) {
      scope.hideQualifyAllForm = function(){
        delete scope.showQualifyAllRegistrantsForm;
      }
    }
  };
});