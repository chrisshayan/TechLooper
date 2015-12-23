techlooper
  .directive('contentRegistrantsTab', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants/contentTab.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('contentEvaluationCriteriaTab', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/evaluation-criteria/contentTab.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
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
        //scope.nextPhase = '';
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

        //scope.$on("$destroy", function () {
        //  if ($rootScope.lastRegistrant) delete $rootScope.lastRegistrant;
        //});

        //scope.registrant.qualify = function () {
        //  //utils.sendNotification(jsonValue.notifications.loading);
        //  delete scope.registrant.disqualified;
        //  delete scope.registrant.disqualifiedReason;
        //  apiService.acceptChallengeRegistrant(scope.registrant.registrantId, scope.registrant.ableAcceptedPhase)
        //    .success(function (registrant) {
        //      scope.registrant.qualified = true;
        //      //$rootScope.$broadcast("update-funnel", registrant);
        //    });
        //
        //  delete scope.registrant.visible;
        //  //utils.sendNotification(jsonValue.notifications.loaded);
        //};

        //scope.registrant.disqualify = function () {
        //  scope.registrant.disqualified = true;
        //  apiService.saveChallengeRegistrant(scope.registrant)
        //    .success(function (rt) {
        //      scope.registrant.qualified = false;
        //      scope.registrant.disqualifiedReason = rt.disqualifiedReason;
        //    });
        //
        //  delete scope.registrant.visible;
        //};

        //scope.registrant.score = function () {
        //  //apiService.acceptChallengeRegistrant(scope.registrant.registrantId)
        //  //    .success(function(registrant) {
        //  //      scope.registrant.activePhase = registrant.activePhase;
        //  //    });
        //  delete scope.registrant.visible;
        //};

        scope.registrant.hide = function () {
          if (!scope.registrant.visible) return;
          delete scope.registrant.visible;
        };
        scope.$on("registrant-qualified", function (sc, submission) {scope.registrant.hide();});
      }
    };
  })
  .directive('tabManagerContestDetail', function (localStorageService, $location) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/tabManager.html",
      link: function (scope, element, attr, ctrl) {
        var params = $location.search();
        if(params.a == "evaluationCriteria"){
          $("a[href='.evaluation-criteria']").tab('show');
        }
        scope.$on("challenge-detail-ready", function () {
          var params = $location.search();
          var showTabRegistrant = (params.a == "registrants") || (params.toPhase != undefined);
          if (showTabRegistrant == true && scope.contestDetail.numberOfRegistrants > 0) {
            $("a[href='.registrants']").tab('show');
          }
        });
      }
    };
  })
  .directive('funnelManager', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/funnelManager.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('contestContentDetails', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestContentDetails.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('registrantList', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('contestDetailReviewSubmission', function (apiService) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/contestDetailReviewSubmission.html",
      link: function (scope, element, attr, ctrl) {
        scope.goToSubmissionLink = function (link) {
          var url = (link.indexOf("https://") >= 0 || link.indexOf("http://") >= 0) ? link : "http://" + link;
          window.open(url, '_newtab');
        }
        //scope.saveReadSubmission = function (submission, isRead) {
        //  apiService.readSubmission(submission.challengeId, submission.challengeSubmissionId, isRead)
        //    .success(function (data) {});
        //}
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
  .directive('evaluationCriteria', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/evaluationCriteria.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  });