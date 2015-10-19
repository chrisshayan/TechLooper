techlooper
  .directive("contestDetailAction", function ($rootScope, apiService, paginationService, utils) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailAction.html",
      scope: {
        registrant: "=",
        challenge: "="
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

        scope.registrant.showScore = function () {
          showView("score");
        }

        scope.registrant.showReviewSubmission = function () {
          showView("reviewSubmission");
        }

        scope.registrant.showFeedback = function () {
          showView("feedbackForm");
        }

        scope.registrant.showAcceptance = function () {
          showView("acceptance");
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

        scope.registrant.score = function() {
          //apiService.acceptChallengeRegistrant(scope.registrant.registrantId)
          //    .success(function(registrant) {
          //      scope.registrant.activePhase = registrant.activePhase;
          //    });
          delete scope.registrant.visible;
        };

        scope.registrant.hide = function() {
          if (!scope.registrant.visible) return;
          delete scope.registrant.visible;
        };

        scope.registrant.accept = function() {
          apiService.acceptChallengeRegistrant(scope.registrant.registrantId)
            .success(function(registrant) {
              scope.registrant.activePhase = registrant.activePhase;
            });
          delete scope.registrant.visible;
        };

        scope.$watch(function () {
          return paginationService.getCurrentPage("__default");
        }, function (currentPage, previousPage) {
          scope.registrant.hide();
        });

        scope.$on("success-submission-challenge", function(sc, submission) {
          if (scope.registrant.registrantId != submission.registrantId) return;
          scope.registrant.submissions.unshift(submission);
          console.log(scope.registrant.submissions);
        });

        utils.sortByNumber(scope.registrant.submissions, "challengeSubmissionId");
      }
    };
  })
  .directive('contestDetailReviewSubmission', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailReviewSubmission.html",
      link: function (scope, element, attr, ctrl){
        scope.goToSubmissionLink = function(link){
          var url = (link.indexOf("https://") >= 0 || link.indexOf("http://") >= 0) ? link : "http://"+link;
          window.open(url, '_newtab');
        }
      }
    };
  })
  .directive('contestDetailDisqualification', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailDisqualification.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('contestDetailQualification', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailQualification.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
 .directive('acceptance', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailAcceptance.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
.directive('contestDetailScore', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/contestDetailScore.html",
    link: function (scope, element, attr, ctrl) {
    }
  };
}).directive('evaluationCriteria', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/evaluationCriteria.html",
    link: function (scope, element, attr, ctrl) {
    }
  };
});