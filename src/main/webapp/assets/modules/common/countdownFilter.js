techlooper.filter("countdown", function (jsonValue, $filter) {
  return function (input, type) {
    if (!input) return "";
    type = type || "day";
    switch (type) {
      case "day":
        if (moment(input, jsonValue.dateFormat).isSame(moment(), "day")) {
          return 1;
        }

        var toNow = moment(input, jsonValue.dateFormat).diff(moment(), "days");
        return toNow + 2;

      case "challenge":
        var contest = input;
        switch (contest.progress.translate) {
          case jsonValue.status.progress.translate:
            return $filter("countdown")(contest.submissionDateTime);

          case jsonValue.status.notStarted.translate:
            return $filter("countdown")(contest.startDateTime);

          case jsonValue.status.registration.translate:
            return $filter("countdown")(contest.registrationDateTime);

          case jsonValue.status.closed.translate:
            return contest.submissionDateTime;
        }
        return "";
    }
  }
});