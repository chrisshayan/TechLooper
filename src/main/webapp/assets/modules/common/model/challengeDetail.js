techlooper.filter("challengeDetail", function (apiService, $rootScope, jsonValue) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challengeDetail = input;
    var index = 0;

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
      challengeDetail.criteria.push({index: index++});
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

    challengeDetail.totalWeight = _.reduceRight(challengeDetail.criteria, function (sum, cri) { return sum + cri.weight; }, 0);

    challengeDetail.validate = function () {
      challengeDetail.$invalid = (challengeDetail.totalWeight != 100);
      $.each(challengeDetail.criteria, function (i, cri) {
        challengeDetail.$invalid = challengeDetail.$invalid || (!cri.name);
        return !challengeDetail.$invalid;
      });
    }

    //@see jsonValue.rewards
    challengeDetail.save1stWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.firstPlaceEnum())
        .success(function (result) {
          if (result == 'true') {
            registrant.reward = jsonValue.rewards.firstPlaceEnum();
            $rootScope.$broadcast("changeWinnerSuccessful", registrant);
          }
        });
    }

    challengeDetail.save2ndWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.secondPlaceEnum())
        .success(function (result) {
          if (result == 'true') {
            registrant.reward = jsonValue.rewards.secondPlaceEnum();
            $rootScope.$broadcast("changeWinnerSuccessful", registrant);
          }
        });
    }

    challengeDetail.save3rdWinner = function (registrant) {
      apiService.saveWinner(registrant.registrantId, jsonValue.rewards.thirdPlaceEnum())
        .success(function (result) {
          if (result == 'true') {
            registrant.reward = jsonValue.rewards.thirdPlaceEnum();
            $rootScope.$broadcast("changeWinnerSuccessful", registrant);
          }
        });
    }

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});