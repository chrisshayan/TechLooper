techlooper.filter("jobseekerDashboardChallenge", function ($filter, $translate, utils, jsonValue) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var challenge = input;

    challenge.recalculate = function () {
      challenge.$asciiChallengeName = utils.toAscii(challenge.challengeName);
      challenge.recalculatePhase();
      challenge.recalculateCriteria();
      challenge.recalculateAward();
      challenge.recalculateSubmissions();
    };

    challenge.recalculatePhase = function () {
      var currentPhaseLowerCase = challenge.currentPhase.toLowerCase();
      challenge.$currentPhaseLowerCase = currentPhaseLowerCase;
      challenge.$currentPhaseTitle = $filter("translate")(currentPhaseLowerCase).toUpperCase();

      challenge.$submissionMdate = moment(challenge.submissionDate, jsonValue.dateFormat);

      var finishDaysLeft = utils.toNow(challenge.$submissionMdate);
      challenge.$daysLeftTitle = finishDaysLeft > 0 ? $filter("translate")("daysLeft", {days: finishDaysLeft}) :
        $filter("translate")("finishedOn", {date: challenge.submissionDate});

      if (challenge.disqualified == true) {
        challenge.$currentPhaseLowerCase = "disqualified";
        challenge.$currentPhaseDescTitle = $filter("translate")("inPhase", {phase: challenge.$currentPhaseTitle});
        challenge.$currentPhaseTitle = $filter("translate")("disqualified").toUpperCase();
      }
      else {
        challenge.$currentPhaseSubmissionDate = moment(challenge.currentPhaseSubmissionDate, jsonValue.dateFormat);
        var submitDaysLeft = utils.toNow(challenge.$currentPhaseSubmissionDate);
        challenge.$currentPhaseDescTitle = finishDaysLeft <= 0 ? undefined : $filter("translate")("daysLeftToSubmit", {days: submitDaysLeft});
      }
    };

    challenge.recalculateSubmissions = function () {
      _.each(challenge.submissions, function (submission) {
        submission.$submissionURL = /^https?:\/\//.test(submission.submissionURL) ? submission.submissionURL : "http://" + submission.submissionURL;
        submission.submissionPhase && (submission.$phaseTitle = $filter("translate")(submission.submissionPhase.toLowerCase()));
      });
    };

    challenge.recalculateAward = function () {
      challenge.$awardLowercase = "price-" + challenge.rank;
      $.isNumeric(challenge.prize) && (challenge.$awardPrizeTitle = "$" + $filter("number")(challenge.prize));
    };

    //TODO refactor
    challenge.recalculateCriteria = function () {
      var totalPoints = 0.0;
      _.each(challenge.criteria, function (cri) {
        var points = (cri.weight / 100) * cri.score;
        cri.$score = cri.score || 0;
        cri.$points = numeral(points).format("0.0");
        totalPoints += points;
      });

      challenge.$totalPoints = numeral(totalPoints).format("0.0");
      challenge.$hasSomeScore = _.some(challenge.criteria, function (cri) {return cri.score != null;});
    };

    challenge.$toggle = {
      reset: function () {
        challenge.$toggleType = false;
      },

      toggleCriteria: function () {
        challenge.$toggleType = "criteria";
      },

      isToggleCriteria: function () {
        return challenge.$toggleType == "criteria";
      },

      toggleSubmissions: function () {
        challenge.$toggleType = "submissions";
      },

      isToggleSubmissions: function () {
        return challenge.$toggleType == "submissions";
      }
    };

    challenge.recalculate();

    challenge.$browserLang = $translate.use();
    challenge.$isRich = true;
    return challenge;
  }
});