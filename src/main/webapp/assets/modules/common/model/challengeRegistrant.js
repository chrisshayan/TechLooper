techlooper.filter("challengeRegistrant", function (apiService, $rootScope, $filter) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var registrant = input;

    var calculatePoint = function(cri) {
      return parseFloat($filter('number')((cri.weight / 100) * cri.score, 1));
    }

    registrant.criteriaLoop = function () {
      var criteria = registrant.criteria;
      if (!criteria) return [];
      registrant.totalPoint = 0;
      return criteria.map(function (cri) {
        cri.point = calculatePoint(cri);
        registrant.totalPoint += parseFloat(cri.point);
        return cri;
      });
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
          registrant.savedTotalPoint = _.reduceRight(registrant.criteria, function (sum, cri) {
            return sum + calculatePoint(cri);
          }, 0);
          registrant.$savedCriteria = true;
          $rootScope.$broadcast("saveRegistrantCriteriaSuccessful", data);
        })
        .error(function () {
          registrant.$savedCriteria = false;
        });
    }

    registrant.savedTotalPoint = _.reduceRight(registrant.criteria, function (sum, cri) {
      return sum + calculatePoint(cri);
    }, 0);

    $rootScope.$on("saveChallengeCriteriaSuccessful", function (scope, challengeCriteriaDto) {
      var criteriaDto = _.findWhere(challengeCriteriaDto.registrantCriteria, {registrantId: registrant.registrantId});
      registrant.criteria = criteriaDto.criteria;
    });

    registrant.$isRich = true;
    return registrant;
  }
});