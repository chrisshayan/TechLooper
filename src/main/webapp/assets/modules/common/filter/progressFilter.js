techlooper.filter("progress", function (jsonValue, resourcesService, localStorageService) {
  var progressFn = function (input, type, param1) {
    if (!input) return "";
    switch (type) {
      case "challenge":
        var contestDetail = input;
        if (contestDetail.progress) return contestDetail.progress.translate;

        var joinContests = localStorageService.get("joinContests") || "";
        var email = localStorageService.get("email") || "";
        var isJoined = (joinContests.indexOf(contestDetail.challengeId) >= 0) && (email.length > 0);
        contestDetail.currentUserJoined = isJoined;

        //if current date < start date
        var startDate = moment(contestDetail.startDateTime, jsonValue.dateFormat);
        contestDetail.progress = angular.copy(jsonValue.status.notStarted);
        if (moment().isBefore(startDate, 'day')) {
          delete contestDetail.currentUserJoined;
          return contestDetail.progress.translate;
        }

        //if start date <= current date <= register date
        var registrationDate = moment(contestDetail.registrationDateTime, jsonValue.dateFormat);
        contestDetail.progress = angular.copy(jsonValue.status.registration);
        if (moment().isBetween(startDate, registrationDate, 'day') || moment().isSame(startDate, 'day') || moment().isSame(registrationDate, 'day')) {
          return contestDetail.progress.translate;
        }

        // if register date < current date <= submit date
        var submissionDate = moment(contestDetail.submissionDateTime, jsonValue.dateFormat);
        contestDetail.progress = angular.copy(jsonValue.status.progress);
        if (moment().isBetween(registrationDate, submissionDate, 'day') || moment().isSame(submissionDate, 'day')) {
          return contestDetail.progress.translate;
        }

        //  if submit date < current date
        contestDetail.progress = angular.copy(jsonValue.status.closed);
        if (submissionDate.isBefore(moment(), 'day')) {
          delete contestDetail.currentUserJoined;
          return contestDetail.progress.translate;
        }

        delete contestDetail.progress;
        delete contestDetail.currentUserJoined;
        return "";

      case "challenges":
        var challenges = input || [];
        var progress = param1;//@see jsonValue.status
        if (!progress) return challenges;

        var progressIds = $.isArray(progress) ? $.map(progress, function (prg) {return prg.id;}).join(",") : progress.id;
        progressIds = progressIds || "";

        return $.grep(challenges, function (challenge, index) {
          if (!challenge.progress) progressFn(challenge, "challenge");
          return progressIds.indexOf(challenge.progress.id) >= 0;
        });

      case "registrantActivePhase":
        var registrant = input;
        if (!registrant.activePhase) return jsonValue.challengePhase.getRegistration().enum;
        return registrant.activePhase;

      case "registrantActivePhaseTitle":
        var registrant = input;
        if (!registrant.activePhase) input = jsonValue.challengePhase.getRegistration();
        if (!input.title) return jsonValue.challengePhase.getEnum(registrant.activePhase).title;
        return input.title;

      case "challengePhaseTitle":
        var phase = input ? input : jsonValue.challengePhase.getRegistration().enum;
        if (!phase.title) return jsonValue.challengePhase.getEnum(phase).title;
        return phase.title;

      case "freelancer-review-project-payment-method":
        var payMethod = input;
        var option = resourcesService.getOption(payMethod, resourcesService.paymentConfig);
        if (!option) return false;
        return option.reviewTranslate;
    }
  }
  return progressFn;
});