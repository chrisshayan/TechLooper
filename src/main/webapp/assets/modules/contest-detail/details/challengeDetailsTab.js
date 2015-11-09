techlooper.directive('prizeAndCurrentPhase', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/details/prizeAndCurrentPhase.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('challengeBasicInformation', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/details/challengeBasicInformation.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  });