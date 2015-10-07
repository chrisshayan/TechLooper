techlooper
  .directive("contestDetailAction", function ($rootScope, apiService, paginationService) {
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

        scope.registrant.showFeedback = function () {
          showView("feedbackForm");
        }

        scope.$on("$destroy", function () {
          if ($rootScope.lastRegistrant) delete $rootScope.lastRegistrant;
        });

        scope.registrant.qualify = function () {
          delete scope.registrant.disqualified;
          delete scope.registrant.disqualifiedReason;
          apiService.saveChallengeRegistrant(scope.registrant);
          delete scope.registrant.visible;
        };

        scope.registrant.disqualify = function () {
          scope.registrant.disqualified = true;
          apiService.saveChallengeRegistrant(scope.registrant)
            .success(function(rt) {
              scope.registrant.disqualifiedReason = rt.disqualifiedReason;
            });
          delete scope.registrant.visible;
        };

        scope.registrant.hide = function() {
          if (!scope.registrant.visible) return;
          delete scope.registrant.visible;
        };

        scope.$watch(function () {
          return paginationService.getCurrentPage("__default");
        }, function (currentPage, previousPage) {
          scope.registrant.hide();
        });
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