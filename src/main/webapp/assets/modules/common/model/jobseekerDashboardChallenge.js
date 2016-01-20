techlooper.filter("jobseekerDashboardChallenge", function ($filter, $translate) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challenge = input;

    challenge.recalculate = function () {
      var currentPhaseLowerCase = challenge.currentPhase.toLowerCase();
      challenge.$currentPhaseLowerCase = currentPhaseLowerCase;
      challenge.$currentPhaseTitle = $filter("translate")(currentPhaseLowerCase);

      challenge.recalculateCriteria();
      challenge.recalculateAward();
    };

    challenge.recalculateAward = function() {
      challenge.$awardLowercase = "price-" + challenge.rank;
      $.isNumeric(challenge.prize) && (challenge.$awardPrizeTitle = "$" + $filter("number")(challenge.prize));
    };

    challenge.recalculateCriteria = function () {
      var totalPoints = 0.0;
      _.each(challenge.criteria, function(cri) {
        var points = (cri.weight / 100) * cri.score;
        cri.$score = cri.score || 0;
        cri.$points = numeral(points).format("0.0");
        totalPoints += points;
      });
      challenge.$totalPoints = numeral(totalPoints).format("0.0");
    };

    challenge.$toggle = {
      reset: function() {
        challenge.$toggleType = false;
      },

      toggleCriteria: function() {
        challenge.$toggleType = "criteria";
      },

      isToggleCriteria: function() {
        return challenge.$toggleType == "criteria";
      },

      toggleSubmissions: function() {
        challenge.$toggleType = "submissions";
      },

      isToggleSubmissions: function() {
        return challenge.$toggleType == "submissions";
      }
    };

    challenge.recalculate();

    challenge.$browserLang = $translate.use();
    challenge.$isRich = true;
    return challenge;
  }
});