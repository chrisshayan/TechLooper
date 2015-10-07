techlooper.directive('reviewSubmittedHistory', function (apiService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/review.html",
    link: function (scope, el, attrs) {

    }
  };
}).directive('disqualifyForm', function (apiService) {
  return {
    restrict: "E",
    replace: true,
    scope: true,
    templateUrl: "modules/contest-detail/disqualify.html",
    link: function (scope, el, attrs) {
      console.log('a', scope.action);
      scope.disqualify = function (registrant) {
        registrant.disqualified = true;
        apiService.saveChallengeRegistrant(registrant)
            .success(function (rt) {
              registrant.disqualified = rt.disqualified;
              registrant.disqualifiedReason = rt.disqualifiedReason;
            }).finally(function () {
              console.log('b', scope.action);
              scope.action = '';
            });
      };
    }
  };
}).directive('qualifyForm', function (apiService) {
  return {
    restrict: "E",
    replace: true,
    scope: true,
    templateUrl: "modules/contest-detail/qualify.html",
    link: function (scope, el, attrs) {
      scope.qualify = function (registrant) {
        console.log('xx', scope.action);
        delete registrant.disqualified;
        delete registrant.disqualifiedReason;
        apiService.saveChallengeRegistrant(registrant)
            .success(function (rt) {
              registrant.disqualified = rt.disqualified;
              registrant.disqualifiedReason = rt.disqualifiedReason;
            }).finally(function () {
              console.log('xx', scope.action);
              scope.action = '';
            });
      };
    }
  };
});