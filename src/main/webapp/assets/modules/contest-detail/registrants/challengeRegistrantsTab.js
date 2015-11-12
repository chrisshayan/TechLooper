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
  .directive('qualifyAllToNextPhase', function (ngProgressFactory, $timeout, apiService) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants/qualifyAllToNextPhase.html",
      link: function (scope, element, attr, ctrl) {
        scope.contestDetail.$filter.byNotQualifiedAndHavingReadSubmissions();

        scope.qualifyRegistrants = function() {
          scope.contestDetail.acceptRegistrants();
          scope.hideQualifyAllForm();
        }

        scope.hideQualifyAllForm = function () {
          delete scope.showQualifyAllRegistrantsForm;
        };
      }
    };
  });