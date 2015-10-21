techlooper.filter("challengeRegistrant", function (apiService, $rootScope) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var registrant = input;

    registrant.criteriaLoop = function () {
      var criteria = registrant.criteria;
      if (!criteria) return [];
      registrant.totalPoint = 0;
      return criteria.map(function (cri) {
        cri.point = cri.weight * cri.score;
        registrant.totalPoint += parseInt(cri.point);
        return cri;
      });
    };

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
          registrant.$savedCriteria = true;
        })
        .error(function () {
          registrant.$savedCriteria = false;
        });
    }

    $rootScope.$on("saveChallengeCriteriaSuccessful", function (scope, challengeCriteriaDto) {
      var criteriaDto = _.findWhere(challengeCriteriaDto.registrantCriteria, {registrantId: registrant.registrantId});
      registrant.criteria = criteriaDto.criteria;
    });

    registrant.$isRich = true;
    return registrant;
  }
});