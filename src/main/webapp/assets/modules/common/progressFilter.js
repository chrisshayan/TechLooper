techlooper.filter("progress", function (jsonValue) {
  return function (input, type) {
    if (!input) return "";
    switch (type) {
      case "challenge":
        var contestDetail = input;
        //if current date < start date
        var startDate = moment(contestDetail.startDateTime, jsonValue.dateFormat);
        contestDetail.progress = jsonValue.status.notStarted;
        if (moment().isBefore(startDate)) {
          return contestDetail.progress.translate;
        }

        //if start date <= current date <= register date
        var registrationDate = moment(contestDetail.registrationDateTime, jsonValue.dateFormat);
        contestDetail.progress = jsonValue.status.registration;
        if (moment().isBetween(startDate, registrationDate) || moment().isSame(startDate) || moment().isSame(registrationDate)) {
          return contestDetail.progress.translate;
        }

        // if register date < current date <= submit date
        var submissionDate = moment(contestDetail.submissionDateTime, jsonValue.dateFormat);
        contestDetail.progress = jsonValue.status.progress;
        if (moment().isBetween(registrationDate, submissionDate) || moment().isSame(submissionDate)) {
          return contestDetail.progress.translate;
        }

        //  if submit date < current date
        contestDetail.progress = jsonValue.status.closed;
        if (submissionDate.isBefore(moment())) {
          return contestDetail.progress.translate;
        }

        delete contestDetail.progress;
        return "";
    }
  }
});