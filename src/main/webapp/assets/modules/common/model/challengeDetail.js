techlooper.filter("challengeDetail", function (apiService, $rootScope, utils) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challengeDetail = input;

    challengeDetail.saveCriteria = function () {
      //if (challengeDetail.$invalidCriteria) return;
      var criteria = {
        challengeId: challengeDetail.challengeId,
        challengeCriteria: challengeDetail.criteria
      }
      delete challengeDetail.$savedCriteria;

      apiService.saveChallengeCriteria(criteria)
        .success(function (data) {
          $rootScope.$broadcast("saveChallengeCriteriaSuccessful", data);
          challengeDetail.$savedCriteria = true;
        })
        .error(function () {
          challengeDetail.$savedCriteria = false;
        });
    }

    challengeDetail.addCriteria = function () {
      challengeDetail.criteria = challengeDetail.criteria || [];
      challengeDetail.criteria.push({});
    }

    challengeDetail.removeCriteria = function (cri) {
      challengeDetail.criteria = _.reject(challengeDetail.criteria, function (criteria) {return criteria.criteriaId == cri.criteriaId;})
    }

    challengeDetail.caculateTotalWeight = function () {
      var total = _.reduceRight(challengeDetail.criteria, function (sum, cri) { return sum + cri.weight; }, 0);
      return total;
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
      $.each(challengeDetail.criteria, function (i, cri) {
        challengeDetail.$invalid = (!cri.name);
        return !challengeDetail.$invalid;
      });
    }

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});