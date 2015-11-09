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
    .directive('ideaList', function () {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/contest-detail/registrants/ideaList.html",
        link: function (scope, element, attr, ctrl) {
        }
      };
    })
.directive('uiuxList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/registrants/uiuxList.html",
    link: function (scope, element, attr, ctrl) {
    }
  };
})
.directive('prototypeList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/registrants/prototypeList.html",
    link: function (scope, element, attr, ctrl) {
    }
  };
})
    .directive('finalAppList', function () {
      return {
        restrict: "E",
        replace: true,
        templateUrl: "modules/contest-detail/registrants/finalAppList.html",
        link: function (scope, element, attr, ctrl) {
        }
      };
    })
.directive('winnerList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/registrants/winnerList.html",
    link: function (scope, element, attr, ctrl) {
    }
  };
}).directive('qualifyAllToNextPhase', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/registrants/qualifyAllToNextPhase.html",
    link: function (scope, element, attr, ctrl) {
    }
  };
});