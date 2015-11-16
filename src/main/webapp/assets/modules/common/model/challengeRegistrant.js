techlooper.filter("challengeRegistrant", function (apiService, $rootScope, jsonValue) {
  return function (input) {
    if (!input || input.$isRich) return input;

    var registrant = input;

    //$rootScope.$on("challenge-criteria-saved", function (scope, challengeCriteriaDto) {
    //  var criteriaDto = _.findWhere(challengeCriteriaDto.registrantCriteria, {registrantId: registrant.registrantId});
    //  registrant.criteria = criteriaDto.criteria;
    //});
    //
    //$rootScope.$on("changeWinnerSuccessful", function (s, challengeDetail) {
    //  registrant.recalculateWinner(challengeDetail);
    //});

    var calculatePoint = function (cri) {
      return (cri.weight / 100) * cri.score;// $filter('number')((cri.weight / 100) * cri.score, 1);
    }

    registrant.refreshCriteria = function () {
      apiService.findRegistrantCriteriaByRegistrantId(registrant.registrantId)
        .success(function (data) {
          if (data.registrantId == registrant.registrantId) {
            registrant.criteria = data.criteria;
          }
        });
    }

    registrant.criteriaLoop = function () {
      var criteria = registrant.criteria;
      if (!criteria) return [];
      registrant.totalPoint = 0;
      criteria = criteria.map(function (cri) {
        cri.point = numeral(calculatePoint(cri)).format("0.0");
        registrant.totalPoint += parseFloat(cri.point);
        return cri;
      });
      registrant.totalPoint = numeral(registrant.totalPoint).format("0.0")
      return criteria;
    };

    registrant.validate = function () {
      delete registrant.$invalid;
      $.each(registrant.criteria, function (i, cri) {
        if (parseInt(cri.score) > 100) {
          registrant.$invalid = true;
        }
        return !registrant.$invalid;
      });
    }

    registrant.saveCriteria = function () {
      var criteria = {
        registrantId: registrant.registrantId,
        criteria: registrant.criteria
      }
      //delete registrant.$savedCriteria;

      _.each(registrant.criteria, function (cri) {(!_.isNumber(cri.score)) && (cri.score = 0);});

      apiService.saveChallengeRegistrantCriteria(criteria)
        .success(function (data) {
          $.each(data.criteria, function (i, cri) {
            var registrantCri = _.findWhere(registrant.criteria, {criteriaId: cri.criteriaId});
            if (registrantCri) {
              registrantCri.score = cri.score;
              registrantCri.comment = cri.comment;
            }
            else {
              registrant.criteria.push(cri);
            }
          });

          registrant.$challengeDetail.incParticipantCount(registrant);

          registrant.savedTotalPoint = numeral(_.reduceRight(registrant.criteria, function (sum, cri) {
            return parseFloat(sum) + parseFloat(calculatePoint(cri));
          }, 0)).format("0.0");

          //registrant.$savedCriteria = true;
          //$rootScope.$broadcast("saveRegistrantCriteriaSuccessful", data);
        });
      //.error(function () {
      //  registrant.$savedCriteria = false;
      //});
    }

    registrant.recalculate = function (challengeDetail) {
      if (registrant.submissions) {
        registrant.lastSubmission = _.isEmpty(registrant.submissions) ? undefined : _.max(registrant.submissions, function (submission) {return submission.challengeSubmissionId;});
      }

      registrant.activePhase = registrant.activePhase ? registrant.activePhase : "REGISTRATION";
      registrant.activePhaseLowerCase = registrant.activePhase.toLowerCase();
      registrant.fullName = registrant.registrantFirstName + " " + registrant.registrantLastName;

      //TODO refactor savedTotalPoint in order to keep the last total point of criteria
      registrant.savedTotalPoint = numeral(_.reduceRight(registrant.criteria, function (sum, cri) {
        return parseFloat(sum) + parseFloat(calculatePoint(cri));
      }, 0)).format("0.0");

      if (challengeDetail) {
        registrant.$challengeDetail = challengeDetail;
        registrant.recalculateDisqualified();
        registrant.recalculateRemainingPhases();
        registrant.recalculateWinner();
      }

      registrant.recalculateSubmissions();
    }

    registrant.recalculateDisqualified = function () {
      var rp = _.findWhere(registrant.$challengeDetail.phaseItems, {phase: registrant.activePhase});
      if (rp.$index > registrant.$challengeDetail.selectedPhaseItem.$index) {
        registrant.disqualified = false;
      }
      if (registrant.disqualified == null) registrant.disqualified = undefined;
    }

    registrant.acceptActivePhase = function (phase) {
      registrant.activePhase = phase;
      registrant.activePhaseLowerCase = registrant.activePhase.toLowerCase();
      registrant.recalculateDisqualified();
      registrant.recalculateRemainingPhases();
    }

    registrant.recalculateSubmissions = function () {
      registrant.$unreadSubmissions = _.reject(registrant.submissions, function (s) {return s.isRead == true;});
      registrant.$isSubmissionsUnread = (registrant.$unreadSubmissions.length == registrant.submissions.length);
      registrant.$readSubmissions = _.filter(registrant.submissions, function (s) {return s.isRead == false || s.isRead == undefined;});
      registrant.$isSubmissionsRead = (registrant.$readSubmissions.length == registrant.submissions.length);
    }

    registrant.recalculateRemainingPhases = function () {
      registrant.remainingPhaseItems = [];
      for (var i = 0; i < registrant.$challengeDetail.phaseItems.length; i++) {
        var cp = registrant.$challengeDetail.phaseItems[i];
        if (cp.phase == registrant.activePhase) {
          var endPhaseItem = _.findWhere(registrant.$challengeDetail.phaseItems, {phase: registrant.$challengeDetail.nextPhase});
          for (var j = i + 1; j <= endPhaseItem.$index; j++) {
            cp = registrant.$challengeDetail.phaseItems[j];
            if (cp.$phaseConfig.isSpecial) continue;
            registrant.remainingPhaseItems.push(cp);
          }
          break;
        }
      }
      //console.log(registrant);
    }

    registrant.recalculateWinner = function () {
      _.extendOwn(registrant, {firstAwarded: false, secondAwarded: false, thirdAwarded: false});

      var rgt = _.findWhere(registrant.$challengeDetail.winners, {registrantId: registrant.registrantId});
      if (!rgt) return;

      registrant.firstAwarded = (rgt.reward == jsonValue.rewards.firstPlaceEnum());
      registrant.secondAwarded = (rgt.reward == jsonValue.rewards.secondPlaceEnum());
      registrant.thirdAwarded = (rgt.reward == jsonValue.rewards.thirdPlaceEnum());
    }

    registrant.acceptSubmission = function (submission) {
      if (!_.findWhere(registrant.submissions, submission)) {
        registrant.submissions.unshift(submission);
        registrant.recalculateSubmissions();
        registrant.$challengeDetail.incSubmissionCount(submission);
        registrant.$challengeDetail.recalculateRegistrants();
      }
    }

    registrant.qualify = function () {
      //utils.sendNotification(jsonValue.notifications.loading);
      //delete registrant.disqualified;
      //delete registrant.disqualifiedReason;
      if (registrant.remainingPhaseItems.length == 1) {
        registrant.selectedPhaseItem = registrant.remainingPhaseItems[0];
      }

      apiService.acceptChallengeRegistrant(registrant.registrantId, registrant.selectedPhaseItem.phase)
        .success(function (rt) {
          //registrant.qualified = !rt.disqualified;
          registrant.activePhase = rt.activePhase;
          registrant.disqualified = false;
          registrant.disqualifiedReason = rt.disqualifiedReason;
          registrant.$challengeDetail.incParticipantCount(registrant);
          $rootScope.$broadcast("registrant-qualified", registrant);
        });

      //delete scope.registrant.visible;
      //utils.sendNotification(jsonValue.notifications.loaded);
    };

    registrant.toggleReadSubmission = function (submission) {
      apiService.readSubmission(submission.challengeId, submission.challengeSubmissionId, !submission.isRead)
        .success(function () {
          submission.isRead = !submission.isRead;
          registrant.recalculateSubmissions();
          registrant.$challengeDetail.incUnreadSubmissionCount(submission);
          //registrant.$challengeDetail.recalculateUnreadSubmissionRegistrant(registrant);
        });
    }

    registrant.disqualify = function () {
      registrant.disqualified = true;
      apiService.rejectChallengeRegistrant(registrant)
        .success(function (rt) {
          registrant.activePhase = rt.activePhase;
          registrant.disqualified = rt.disqualified;
          registrant.disqualifiedReason = rt.disqualifiedReason;
          registrant.$challengeDetail.incParticipantCount(registrant);
          $rootScope.$broadcast("registrant-qualified", registrant);
        });

      //delete scope.registrant.visible;
    };

    registrant.criteriaLoop();//calc totalPoint
    //console.log(registrant);

    registrant.$isRich = true;
    return registrant;
  }
});