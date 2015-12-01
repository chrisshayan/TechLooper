techlooper.filter("challengeDetail", function (apiService, $rootScope, jsonValue, $filter, $q, localStorageService) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challengeDetail = input;


    challengeDetail.$filter = {

      //filter all registrants not qualified and having all submissions read
      byNotQualifiedAndHavingReadSubmissions: function () {
        challengeDetail.$filter.qualifyRegistrantsType = {isHavingReadSubmission: true};
        challengeDetail.$filter.qualifyRegistrants = _.filter(challengeDetail.$registrants, function (rgt) {
          return rgt.disqualified == undefined && rgt.submissions.length > 0 && rgt.$isSubmissionsRead;
        });
      },

      //filter all registrants not qualified and having submissions
      byNotQualifiedAndHavingSubmissions: function () {
        challengeDetail.$filter.qualifyRegistrantsType = {isHavingSubmission: true};
        challengeDetail.$filter.qualifyRegistrants = _.filter(challengeDetail.$registrants, function (rgt) {
          return rgt.disqualified == undefined && rgt.submissions.length > 0;
        });
      },

      //filter all registrants not qualified
      byNotQualified: function () {
        challengeDetail.$filter.qualifyRegistrantsType = {isNotQualified: true};
        challengeDetail.$filter.qualifyRegistrants = _.filter(challengeDetail.$registrants, function (rgt) {return rgt.disqualified == undefined;});
      },

      registrantsType: {isUnread: false},//flag to known which type of challengeDetail.$filter.registrants, default is show all registrants
      byReadOrUnreadSubmission: function (isUnread) {//filter registrants all/unread submission
        isUnread = (isUnread != undefined) ? isUnread : challengeDetail.$filter.registrantsType.isUnread;
        challengeDetail.$filter.registrantsType = {isUnread: isUnread};
        challengeDetail.$filter.registrants = isUnread ? _.filter(challengeDetail.$registrants, function (r) {return r.$unreadSubmissions.length > 0;}) : challengeDetail.$registrants;
      }
    }

    challengeDetail.$sort = {
      type: {},

      //sort filter-registrants by submissions
      bySubmissionCount: function () {
        challengeDetail.$sort.type = {isSubmissionCountTypeAsc: !challengeDetail.$sort.type.isSubmissionCountTypeAsc};
        challengeDetail.$registrants = _(challengeDetail.$registrants).chain().sortBy("submissions.length").reverse().value();
        challengeDetail.$filter.byReadOrUnreadSubmission();
      },

      //sort filter-registrants by last submission
      byLastSubmission: function (sortType) {
        if (sortType != undefined) {
          challengeDetail.$sort.type = {isLastSubmissionTypeAsc: sortType};
        }
        else {
          challengeDetail.$sort.type = {isLastSubmissionTypeAsc: !challengeDetail.$sort.type.isLastSubmissionTypeAsc};
        }

        challengeDetail.$registrants.sort(function(r1, r2) {
          var int = challengeDetail.$sort.type.isLastSubmissionTypeAsc ? 1 : -1;
          var n1 = (r1.lastSubmission ? r1.lastSubmission.challengeSubmissionId : 0);
          var n2 = (r2.lastSubmission ? r2.lastSubmission.challengeSubmissionId : 0);
          return int * (n1 - n2);
        });

        challengeDetail.$filter.byReadOrUnreadSubmission();
      },

      //sort filter-registrants by total point
      byTotalPoint: function (sortType) {
        if (sortType != undefined) {
          challengeDetail.$sort.type = {isTotalPointTypeAsc: sortType};
        }
        else {
          challengeDetail.$sort.type = {isTotalPointTypeAsc: !challengeDetail.$sort.type.isTotalPointTypeAsc};
        }

        challengeDetail.$registrants.sort(function(r1, r2) {
          var int = challengeDetail.$sort.type.isTotalPointTypeAsc ? 1 : -1;
          var n1 = parseFloat(r1.totalPoint ? r1.totalPoint : 0);
          var n2 = parseFloat(r2.totalPoint ? r2.totalPoint : 0);
          return int * (n1 - n2);
        });

        challengeDetail.$filter.byReadOrUnreadSubmission();
      },

      //sort filter-registrants by total point
      byRegistrationDate: function (sortType) {
        if (sortType != undefined) {
          challengeDetail.$sort.type = {isRegistrationDateTypeAsc: sortType}
        }
        else {
          challengeDetail.$sort.type = {isRegistrationDateTypeAsc: !challengeDetail.$sort.type.isRegistrationDateTypeAsc};
        }

        challengeDetail.$registrants.sort(function(r1, r2) {
          var int = challengeDetail.$sort.type.isRegistrationDateTypeAsc ? 1 : -1;
          var n1 = (r1.registrantId ? r1.registrantId : 0);
          var n2 = (r2.registrantId ? r2.registrantId : 0);
          return int * (n1 - n2);
        });

        challengeDetail.$filter.byReadOrUnreadSubmission();
      }
    }

    challengeDetail.refreshCriteria = function () {
      apiService.getContestDetail(challengeDetail.challengeId)
        .success(function (data) {
          challengeDetail.criteria = data.criteria;
        });
    }

    challengeDetail.refreshRegistrants = function () {
      var defer = $q.defer();
      $rootScope.$broadcast("before-getting-registrants", challengeDetail);
      apiService.getChallengeRegistrantsByPhase(challengeDetail.challengeId, challengeDetail.selectedPhaseItem.$phaseConfig.enum)
        .success(function (registrants) {
          challengeDetail.recalculateRegistrants(registrants);
          defer.resolve();
        })
        .error(function() {
          challengeDetail.$error = challengeDetail.$error || {};
          challengeDetail.$error.registrants = true;
        })
        .finally(function () {
          $rootScope.$broadcast("after-getting-registrants", challengeDetail);
        });
      return defer.promise;
    }

    challengeDetail.saveCriteria = function () {
      challengeDetail.validateCriteria();
      if (challengeDetail.$invalidCriteria) return false;

      var criteria = {
        challengeId: challengeDetail.challengeId,
        challengeCriteria: challengeDetail.criteria
      }

      apiService.saveChallengeCriteria(criteria)
        .success(function (criteria) {
          //challengeDetail.$savedCriteria = true;
          $rootScope.$broadcast("challenge-criteria-saved", criteria);
        });
      //.error(function () {
      //  challengeDetail.$savedCriteria = false;
      //});
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

    challengeDetail.validateCriteria = function () {
      challengeDetail.$invalidCriteria = (challengeDetail.totalWeight != 100);
      $.each(challengeDetail.criteria, function (i, cri) {
        challengeDetail.$invalidCriteria = challengeDetail.$invalidCriteria || (!cri.name);
        return !challengeDetail.$invalidCriteria;
      });
    }

    //@see jsonValue.rewards
    challengeDetail.save1stWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.firstPlaceEnum(), !registrant.firstAwarded)
        .success(challengeDetail.updateWinners);
    }

    challengeDetail.save2ndWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.secondPlaceEnum(), !registrant.secondAwarded)
        .success(challengeDetail.updateWinners);
    }

    challengeDetail.save3rdWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.thirdPlaceEnum(), !registrant.thirdAwarded)
        .success(challengeDetail.updateWinners);
    }

    challengeDetail.updateWinners = function (winners) {
      challengeDetail.winners = winners;
      challengeDetail.recalculateRegistrants();
      challengeDetail.refreshFunnelItems();
    }

    challengeDetail.recalculate = function (registrants) {

      //see jsonValue.challengePhase
      var prop = jsonValue.challengePhase[challengeDetail.currentPhase].challengeProp;
      if (prop) {
        var date = moment(challengeDetail[prop], jsonValue.dateFormat);
        challengeDetail.currentPhaseDaysLeft = date.diff(moment(0, "HH"), "days") + 1;
      }

      var isOver = true;
      _.each(challengeDetail.phaseItems, function (item, index) {
        item.$index = index;
        item.isOver = isOver;
        item.phaseLowerCase = item.phase.toLowerCase();

        var cp = _.findWhere(jsonValue.challengePhase.values, {enum: item.phase});
        item.$phaseConfig = cp;
        challengeDetail.recalculatePhaseItem(item);

        if (item.phase == challengeDetail.currentPhase) {
          item.isCurrentPhase = true;
          isOver = false;
        }
        if (item.phase == challengeDetail.nextPhase) {
          item.isNextPhase = true;
        }
      });

      // make un-selectable phase from current-phase + 2
      var current = _.findWhere(challengeDetail.phaseItems, {isCurrentPhase: true});
      if (challengeDetail.isClosed) {
        current.isCurrentPhase = false;
        _.last(challengeDetail.phaseItems).isCurrentPhase = true;//auto select winner if challenge is closed
      }
      for (var i = current.$index + 2; i < challengeDetail.phaseItems.length - 1; i++) {// winner tab is selectable
        challengeDetail.phaseItems[i].unselectable = true;
      }

      challengeDetail.totalWeight = _.reduceRight(challengeDetail.criteria, function (sum, cri) { return sum + cri.weight; }, 0);

      //var phaseName = localStorageService.get("toPhase");
      //phaseName && challengeDetail.setSelectedPhase(phaseName);
      //localStorageService.remove("toPhase");
      //
      //if (!challengeDetail.selectedPhaseItem) {
      //  challengeDetail.setSelectedPhase(challengeDetail.isClosed ? "WINNER" : challengeDetail.currentPhase)
      //}

      if (_.isArray(registrants)) {
        challengeDetail.recalculateRegistrants(registrants);
      }
    }

    challengeDetail.recalculateRegistrants = function (registrants) {
      if (registrants) challengeDetail.$registrants = registrants;
      _.each(challengeDetail.$registrants, function (rgt, index) {
        rgt.$index = index;
        rgt.recalculate(challengeDetail);
      });

      //winner phase
      //var winnerPi = _.last(challengeDetail.phaseItems);
      //challengeDetail.recalculatePhaseItem(winnerPi);
      //
      //challengeDetail.recalculateHadRegistrant();
    }

      //set $hadRegistrant to true if not found any registrant that unknown disqualified-status
      //console.log(challengeDetail.$registrants);
    challengeDetail.recalculateHadRegistrant = function() {
      console.log(challengeDetail.$registrants);
      var er = _.findWhere(challengeDetail.$registrants, {disqualified: undefined});
      challengeDetail.$hadRegistrant = (er == undefined);
      challengeDetail.$filter.byReadOrUnreadSubmission();
    }

    challengeDetail.recalculatePhaseItem = function (phaseItem) {
      var piTranslate = phaseItem.$phaseConfig.phaseItem.translate;
      phaseItem.countJoinerTitle = $filter("translate")(piTranslate.countJoiner, {number: phaseItem.participant});
      phaseItem.countSubmissionTitle = $filter("translate")(piTranslate.countSubmission, {number: phaseItem.submission});
      phaseItem.countUnreadTitle = $filter("translate")(piTranslate.countUnread, {number: phaseItem.unreadSubmission});
    }

    challengeDetail.recalculateWinner = function (registrant) {
      if (!_.findWhere(challengeDetail.phaseItems, {phase: registrant.activePhase}).$phaseConfig.isFinal) return;

      var finalRegistrants = _.where(challengeDetail.$registrants, {activePhase: "FINAL"});
      var countWinnerPaticipants = 0;
      _.each(finalRegistrants, function (registrant) {
        if (registrant.disqualified == true) return;
        var count = _.countBy(registrant.criteria, function (cri) {
          return _.isNumber(cri.score) ? "hasScore" : "notHasScore";
        });
        countWinnerPaticipants += (count.hasScore > 0) ? 1 : 0;
      });

      var winnerPi = _.last(challengeDetail.phaseItems);
      winnerPi.participant = countWinnerPaticipants;
      challengeDetail.recalculatePhaseItem(winnerPi);
    }

    challengeDetail.refreshFunnelItems = function() {
      apiService.getRegistrantFunnel(challengeDetail.challengeId)
        .success(function(items) {
          for (var i = 0; i < challengeDetail.phaseItems.length; i++) {
            _.extendOwn(challengeDetail.phaseItems[i], items[i]);
            challengeDetail.recalculatePhaseItem(challengeDetail.phaseItems[i]);
          }
          challengeDetail.recalculateHadRegistrant();
        });
    }

    challengeDetail.incUnreadSubmissionCount = function (submission) {
      var pi = _.findWhere(challengeDetail.phaseItems, {phase: submission.submissionPhase});
      submission.isRead ? pi.unreadSubmission-- : pi.unreadSubmission++;
      challengeDetail.recalculatePhaseItem(pi);
    }


    // see com.techlooper.model.ChallengeRegistrantFunnelItem
    challengeDetail.setSelectedPhase = function (phaseItem) {
      if (_.isString(phaseItem)) {
        phaseItem = _.findWhere(challengeDetail.phaseItems, {phase: phaseItem});
      }

      if (!phaseItem) {
        return challengeDetail.setSelectedPhase(challengeDetail.isClosed ? "WINNER" : challengeDetail.currentPhase);
      }

      if (phaseItem.unselectable) return;

      challengeDetail.phaseItems.map(function (item) {item.isSelected = false;});
      challengeDetail.selectedPhaseItem = phaseItem;
      var nextPhaseItem = _.findWhere(challengeDetail.phaseItems, {isNextPhase: true});
      challengeDetail.$rgtNextPhaseItem = phaseItem.$phaseConfig.isFinal ? undefined :
        challengeDetail.phaseItems[_.min([phaseItem.$index + 1, nextPhaseItem.$index])];
      phaseItem.isSelected = true;

      challengeDetail.refreshRegistrants()
        .then(function () {
          //console.log(challengeDetail.$registrants);
          //TODO refactor default sort`
          if (phaseItem.$phaseConfig.isRegistration) {
            challengeDetail.$sort.byRegistrationDate(false);
          }
          else if (phaseItem.$phaseConfig.isIdea || phaseItem.$phaseConfig.isUiux || phaseItem.$phaseConfig.isPrototype) {
            challengeDetail.$sort.byLastSubmission(false);
          }
          else {
            challengeDetail.$sort.byTotalPoint(false);
          }
        });

      challengeDetail.refreshFunnelItems();
    }

    challengeDetail.acceptRegistrants = function () {
      var registrantIds = _.map(challengeDetail.$filter.qualifyRegistrants, function (rgt) {return rgt.registrantId;});
      if (registrantIds.length == 0) return;
      apiService.qualifyAllToNextPhase(challengeDetail.challengeId, challengeDetail.$rgtNextPhaseItem.phase, registrantIds)
        .success(function (registrants) {
          var count = 0;
          $.each(challengeDetail.$registrants, function (i, cr) {
            var r = _.findWhere(registrants, {registrantId: cr.registrantId});
            if (r) {
              count++;
              cr.acceptActivePhase(r.activePhase);
              //challengeDetail.incParticipantCount(cr);
              challengeDetail.recalculateRegistrants();
            }
            if (count == registrants.length) return false;
          });
          challengeDetail.refreshFunnelItems();
        });
    }

    challengeDetail.recalculate();

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});