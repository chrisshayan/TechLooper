techlooper.filter("challengeDetail", function (apiService) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challengeDetail = input;

    challengeDetail.saveCriteria = function() {
      var criteria = {
        challengeId: challengeDetail.challengeId,
        challengeCriteria: challengeDetail.criteria
      }

      apiService.saveChallengeCriteria(criteria)
        .success(function(data) {
          console.log(data);
        });
    }

    challengeDetail.$isRich = true;
    return challengeDetail;
  }
});