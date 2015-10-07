techlooper
  .directive("contestDetailAction", function ($rootScope, paginationService) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailAction.html",
      scope: {
        registrant: "="
      },
      link: function (scope, element, attr, ctrl) {
        var showView = function (view) {
          if ($rootScope.$eval("lastRegistrant.visible")) {
            console.log($rootScope.lastRegistrant);
            delete $rootScope.lastRegistrant.visible;
          }
          scope.registrant.visible = {};
          scope.registrant.visible[view] = true;
          $rootScope.lastRegistrant = scope.registrant;
        }

        scope.registrant.showDisqualification = function () {
          showView("disqualification");
        }

        scope.registrant.showQualification = function () {
          showView("qualification");
        }

        scope.registrant.showReviewSubmission = function () {
          showView("reviewSubmission");
        }

        scope.$on("$destroy", function () {
          if ($rootScope.lastRegistrant) delete $rootScope.lastRegistrant;
        });

        //scope.$watch(function () {
        //  return paginationService.getCurrentPage("__default");
        //}, function (currentPage, previousPage) {
        //  //if (currentPage != previousPage) {
        //  //
        //  //}
        //});
      }
    };
  })
  .directive('contestDetailReviewSubmission', function (apiService) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailReviewSubmission.html"
    };
  })
  .directive('contestDetailDisqualification', function (apiService) {
    return {
      restrict: "E",
      replace: true,
      scope: true,
      templateUrl: "modules/contest-detail/contestDetailDisqualification.html",
      link: function (scope, element, attr, ctrl) {
        //console.log('a', scope.action);
        //scope.disqualify = function (registrant) {
        //  registrant.disqualified = true;
        //  apiService.saveChallengeRegistrant(registrant)
        //    .success(function (rt) {
        //      registrant.disqualified = rt.disqualified;
        //      registrant.disqualifiedReason = rt.disqualifiedReason;
        //    }).finally(function () {
        //    console.log('b', scope.action);
        //    scope.action = '';
        //  });
        //};
      }
    };
  })
  .directive('contestDetailQualification', function (apiService) {
    return {
      restrict: "E",
      replace: true,
      scope: true,
      templateUrl: "modules/contest-detail/contestDetailQualification.html",
      link: function (scope, element, attr, ctrl) {
        //scope.qualify = function (registrant) {
        //  console.log('xx', scope.action);
        //  delete registrant.disqualified;
        //  delete registrant.disqualifiedReason;
        //  apiService.saveChallengeRegistrant(registrant)
        //    .success(function (rt) {
        //      registrant.disqualified = rt.disqualified;
        //      registrant.disqualifiedReason = rt.disqualifiedReason;
        //    }).finally(function () {
        //    console.log('xx', scope.action);
        //    scope.action = '';
        //  });
        //};
      }
    };
  });