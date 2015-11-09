techlooper.filter("challengeDetail", function (apiService, $rootScope, jsonValue, $filter, $translate) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challengeDetail = input;
    //var index = 0;
    //challengeDetail.criteriaIndex = 0;

    challengeDetail.refreshCriteria = function () {
      apiService.getContestDetail(challengeDetail.challengeId)
        .success(function (data) {
          challengeDetail.criteria = data.criteria;
        });
    }

    challengeDetail.saveCriteria = function () {
      var criteria = {
        challengeId: challengeDetail.challengeId,
        challengeCriteria: challengeDetail.criteria
      }
      delete challengeDetail.$savedCriteria;

      apiService.saveChallengeCriteria(criteria)
        .success(function (data) {
          challengeDetail.$savedCriteria = true;
          $rootScope.$broadcast("saveChallengeCriteriaSuccessful", data);
        })
        .error(function () {
          challengeDetail.$savedCriteria = false;
        });
    }

    challengeDetail.addCriteria = function () {
      challengeDetail.criteria = challengeDetail.criteria || [];
      challengeDetail.criteria.push({index: challengeDetail.criteria.length + 1});
    }

    challengeDetail.removeCriteria = function (cri) {
      challengeDetail.criteria = _.reject(challengeDetail.criteria, function (criteria) {
        if (cri.criteriaId) return criteria.criteriaId == cri.criteriaId;
        return criteria.index == cri.index;
      });
    }

    challengeDetail.criteriaLoop = function () {
      var criteria = challengeDetail.criteria;
      if (!criteria) return [];
      challengeDetail.totalWeight = 0;
      return criteria.map(function (cri) {
        var weight = _.isNumber(cri.weight) ? cri.weight : 0;
        challengeDetail.totalWeight += weight;
        return cri;
      });
    };

    //challengeDetail.totalWeight = _.reduceRight(challengeDetail.criteria, function (sum, cri) { return sum + cri.weight; }, 0);

    challengeDetail.validate = function () {
      challengeDetail.$invalid = (challengeDetail.totalWeight != 100);
      $.each(challengeDetail.criteria, function (i, cri) {
        challengeDetail.$invalid = challengeDetail.$invalid || (!cri.name);
        return !challengeDetail.$invalid;
      });
    }

    //@see jsonValue.rewards
    challengeDetail.save1stWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.firstPlaceEnum(), !registrant.firstAwarded)
        .success(function (winners) {
          challengeDetail.winners = winners;
          $rootScope.$broadcast("changeWinnerSuccessful", challengeDetail)
        });
    }

    challengeDetail.save2ndWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.secondPlaceEnum(), !registrant.secondAwarded)
        .success(function (winners) {
          challengeDetail.winners = winners;
          $rootScope.$broadcast("changeWinnerSuccessful", challengeDetail)
        });
    }

    challengeDetail.save3rdWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.thirdPlaceEnum(), !registrant.thirdAwarded)
        .success(function (winners) {
          challengeDetail.winners = winners;
          $rootScope.$broadcast("changeWinnerSuccessful", challengeDetail)
        });
    }

    challengeDetail.recalculate = function (phaseName, registrants) {
      if (!phaseName) {
        phaseName = challengeDetail.selectedPhase.phase;
      }

      //see jsonValue.challengePhase
      var prop = jsonValue.challengePhase[challengeDetail.currentPhase].challengeProp;
      if (prop) {
        var date = moment(challengeDetail[prop], jsonValue.dateFormat);
        challengeDetail.currentPhaseDaysLeft = date.diff(moment(0, "HH"), "days") + 1;
      }

      if (phaseName) {
        challengeDetail.recalculateRegistrantRemainsPhases(phaseName);
      }

      if (registrants) {
        _.each(registrants, function (rgt) {
          rgt.ableAcceptedPhase = challengeDetail.nextPhase;
        });

        challengeDetail.recalculateStateWinners(registrants);
      }

      if (challengeDetail.phaseItems) {
        var isOver = true;
        _.each(challengeDetail.phaseItems, function (item, index) {
          item.isOver = isOver;
          item.phaseLowerCase = item.phase.toLowerCase();

          var cp = _.findWhere(jsonValue.challengePhase.values, {enum: item.phase});
          item.countJoinerTitle = $filter("translate")(cp.phaseItem.translate.countJoiner, {number: item.participant});
          item.countSubmissionTitle = $filter("translate")(cp.phaseItem.translate.countSubmission, {number: item.submission});

          if (item.phase == challengeDetail.currentPhase) {
            item.isCurrentPhase = true;
            item.index = index;
            isOver = false;
          }
        });

        // mark unclickable from current-phase + 2
        var current = _.findWhere(challengeDetail.phaseItems, {isCurrentPhase: true});
        for (var i = current.index + 2; i < challengeDetail.phaseItems.length; i++) {
          challengeDetail.phaseItems[i].unclickable = true;
        }
      }

      challengeDetail.totalWeight = _.reduceRight(challengeDetail.criteria, function (sum, cri) { return sum + cri.weight; }, 0);
    }

    challengeDetail.recalculateStateWinners = function (registrants) {
      var finalRegistrants = _.where(registrants, {"activePhase": jsonValue.challengePhase.getLastPhase().enum});
      challengeDetail.countWinnerPaticipants = 0;
      _.each(finalRegistrants, function (registrant) {
        if (registrant.disqualified == true) return;
        var count = _.countBy(registrant.criteria, function (cri) {
          return _.isNumber(cri.score) ? "hasScore" : "notHasScore";
        });
        challengeDetail.countWinnerPaticipants += (count.hasScore > 0) ? 1 : 0;
      })
    }

    // see jsonValue.challengePhase
    challengeDetail.recalculateRegistrantRemainsPhases = function (phase) {
      challengeDetail.registrantRemainsPhases = [];
      if (!challengeDetail.phaseItems || phase == challengeDetail.nextPhase) return;

      var alreadyNext = false;
      for (var i = 0; i < challengeDetail.phaseItems.length; i++) {
        if (challengeDetail.phaseItems[i].phase == phase) {
          for (var j = i + 1; j < challengeDetail.phaseItems.length; j++) {
            if (alreadyNext) break;
            challengeDetail.registrantRemainsPhases.push(challengeDetail.phaseItems[j].phase);
            if (challengeDetail.phaseItems[j].phase == challengeDetail.nextPhase) {
              alreadyNext = true;
            }
          }
          break;
        }
      }
    }

    // see com.techlooper.model.ChallengeRegistrantFunnelItem
    challengeDetail.setSelectedPhase = function (phaseItem) {
      if (_.isString(phaseItem)) {
        phaseItem = _.findWhere(challengeDetail.phaseItems, {phase: phaseItem});
      }

      if (phaseItem.unclickable) return;

      challengeDetail.phaseItems.map(function (item) {item.isSelected = false;});
      challengeDetail.selectedPhase = phaseItem;
      phaseItem.isSelected = true;
      challengeDetail.recalculate();
    }

    challengeDetail.setSelectedPhase(challengeDetail.currentPhase);
    challengeDetail.recalculate();

    console.log(challengeDetail);

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});