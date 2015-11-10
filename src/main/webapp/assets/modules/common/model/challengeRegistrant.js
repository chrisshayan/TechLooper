techlooper.filter("challengeRegistrant", function (apiService, $rootScope, jsonValue) {
  return function (input) {
    if (!input || input.$isRich) return input;

    var registrant = input;

    //$rootScope.$on("saveChallengeCriteriaSuccessful", function (scope, challengeCriteriaDto) {
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
      delete registrant.$savedCriteria;

      _.each(registrant.criteria, function (cri) {
        (!_.isNumber(cri.score)) && (cri.score = 0);
      });

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
          registrant.savedTotalPoint = numeral(_.reduceRight(registrant.criteria, function (sum, cri) {
            return parseFloat(sum) + parseFloat(calculatePoint(cri));
          }, 0)).format("0.0");
          registrant.$savedCriteria = true;
          $rootScope.$broadcast("saveRegistrantCriteriaSuccessful", data);
        })
        .error(function () {
          registrant.$savedCriteria = false;
        });
    }

    registrant.savedTotalPoint = numeral(_.reduceRight(registrant.criteria, function (sum, cri) {
      return parseFloat(sum) + parseFloat(calculatePoint(cri));
    }, 0)).format("0.0");

    registrant.recalculate = function (challengeDetail) {
      if (registrant.submissions) {
        registrant.lastSubmission = _.isEmpty(registrant.submissions) ? undefined : _.max(registrant.submissions, function (submission) {return submission.challengeSubmissionId;});
        //registrant.phaseSubmissions = _.filter(registrant.submissions, function (submission) {return submission.submissionPhase == challengePhase;});
      }

      registrant.activePhase = registrant.activePhase ? registrant.activePhase : challengeDetail.currentPhase;
      //if (challengePhase != registrant.activePhase) {
      //  registrant.qualified = true;
      //}
      //else if (registrant.disqualified == true) {
      //  registrant.qualified = false;
      //}

      //if (!registrant.activePhase) registrant.activePhase = "REGISTRATION";

      registrant.fullName = registrant.registrantFirstName + " " + registrant.registrantLastName;

      if (registrant.disqualified == undefined) {
        var rp = _.findWhere(challengeDetail.phaseItems, {phase: registrant.activePhase});
        if (rp.$index > challengeDetail.selectedPhaseItem.$index) {
          registrant.disqualified = false;
        }
      }

      //console.log(registrant);
    }

    registrant.recalculateWinner = function (challengeDetail) {
      _.extendOwn(registrant, {firstAwarded: false, secondAwarded: false, thirdAwarded: false});

      var rgt = _.findWhere(challengeDetail.winners, {registrantId: registrant.registrantId});
      if (!rgt) return;

      registrant.firstAwarded = (rgt.reward == jsonValue.rewards.firstPlaceEnum());
      registrant.secondAwarded = (rgt.reward == jsonValue.rewards.secondPlaceEnum());
      registrant.thirdAwarded = (rgt.reward == jsonValue.rewards.thirdPlaceEnum());
    }

    registrant.acceptSubmission = function (submission) {
      if (!_.findWhere(registrant.submissions, submission)) {
        registrant.submissions.unshift(submission);
        registrant.recalculate(submission.submissionPhase);
      }
    }

    registrant.qualify = function () {
      //utils.sendNotification(jsonValue.notifications.loading);
      //delete registrant.disqualified;
      //delete registrant.disqualifiedReason;
      apiService.acceptChallengeRegistrant(registrant.registrantId, registrant.ableAcceptedPhase)
        .success(function (rt) {
          //registrant.qualified = !rt.disqualified;
          registrant.disqualified = rt.disqualified;
          registrant.disqualifiedReason = rt.disqualifiedReason;
        });
      //.finally(function() {
      //  $rootScope.$broadcast("registrant-qualified", registrant);
      //});

      //delete scope.registrant.visible;
      //utils.sendNotification(jsonValue.notifications.loaded);
    };

    registrant.disqualify = function () {
      registrant.disqualified = true;
      apiService.saveChallengeRegistrant(registrant)
        .success(function (rt) {
          registrant.disqualified = rt.disqualified;
          registrant.disqualifiedReason = rt.disqualifiedReason;
        });
      //.finally(function() {
      //  $rootScope.$broadcast("registrant-disqualified", registrant);
      //});

      //delete scope.registrant.visible;
    };

    //registrant.recalculate(challengePhase);
    registrant.criteriaLoop();

    registrant.$isRich = true;
    return registrant;
  }
});