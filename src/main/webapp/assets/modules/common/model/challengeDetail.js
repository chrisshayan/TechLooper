techlooper.filter("challengeDetail", function (apiService, $rootScope) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challengeDetail = input;

    challengeDetail.saveCriteria = function() {
      var criteria = {
        challengeId: challengeDetail.challengeId,
        challengeCriteria: challengeDetail.criteria
      }
      delete challengeDetail.$savedCriteria;

      apiService.saveChallengeCriteria(criteria)
        .success(function(data) {
          $rootScope.$broadcast("saveChallengeCriteriaSuccessful", data);
          challengeDetail.$savedCriteria = true;
        })
        .error(function() {
          challengeDetail.$savedCriteria = false;
        });
    }

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});