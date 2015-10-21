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

    //TODO this function invoked 15 times in first load, then many times when change criteria
    challengeDetail.totalWeight = function() {
      //console.log(arguments);
      var total = _.reduceRight(challengeDetail.criteria, function(sum, cri) { return sum + cri.weight; }, 0);
      //console.log(total);
      //challengeDetail.$invalid = (total == 100);
      return total;
    }

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});