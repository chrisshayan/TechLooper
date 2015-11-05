techlooper.filter("challengeRegistrant", function (apiService, $rootScope, jsonValue) {
  return function (input, challengePhase) {
    if (!input || input.$isRich) return input;

    var registrant = input;

    $rootScope.$on("saveChallengeCriteriaSuccessful", function (scope, challengeCriteriaDto) {
      var criteriaDto = _.findWhere(challengeCriteriaDto.registrantCriteria, {registrantId: registrant.registrantId});
      registrant.criteria = criteriaDto.criteria;
    });

    $rootScope.$on("changeWinnerSuccessful", function (s, challengeDetail) {
      registrant.recalculateWinner(challengeDetail);
    });

    var calculatePoint = function (cri) {
      return (cri.weight / 100) * cri.score;// $filter('number')((cri.weight / 100) * cri.score, 1);
    }

    registrant.fullName = registrant.registrantLastName + " " + registrant.registrantFirstName;

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

    registrant.recalculate = function (challengePhase) {
      if (registrant.submissions) {
        registrant.lastSubmission = _.isEmpty(registrant.submissions) ? undefined : _.max(registrant.submissions, function (submission) {return submission.challengeSubmissionId;});
        //registrant.phaseSubmissions = _.filter(registrant.submissions, function (submission) {return submission.submissionPhase == challengePhase;});
      }

      registrant.activePhase = registrant.activePhase ? registrant.activePhase : jsonValue.challengePhase.getRegistration().enum;
      if (challengePhase != registrant.activePhase) {
        registrant.qualified = true;
      }
      else if (registrant.disqualified == true) {
        registrant.qualified = false;
      }

      if (!registrant.activePhase) registrant.activePhase = jsonValue.challengePhase.getRegistration().enum;
    }

    registrant.recalculateWinner = function(challengeDetail) {
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

    registrant.recalculate(challengePhase);

    registrant.$isRich = true;
    return registrant;
  }
});