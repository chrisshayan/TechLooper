techlooper.filter("challengeRegistrant", function (apiService) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var registrant = input;

    registrant.saveCriteria = function() {
      var criteria = {
        registrantId: registrant.registrantId,
        criteria: registrant.criteria
      }
      apiService.saveChallengeRegistrantCriteria(criteria)
        .success(function(data) {
          $.each(data.criteria, function(i, cri) {
            var registrantCri = _.findWhere(registrant.criteria, {criteriaId: cri.criteriaId});
            if (registrantCri) {
              registrantCri.score = cri.score;
              registrantCri.comment = cri.comment;
            }
            else {
              registrant.criteria.push(cri);
            }
          });
        });
    }

    registrant.$isRich = true;
    return registrant;
  }
});