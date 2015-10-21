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
        .success(function(criteria) {
          console.log(criteria);
          //$.each(criteria, function(i, cri) {
          //  var registrantCri = _.findWhere(registrant.criteria, {criteriaId: cri.criteriaId});
          //  if (registrantCri) {
          //    registrantCri.score = cri.score;
          //  }
          //});
        });
    }

    registrant.$isRich = true;
    return registrant;
  }
});