techlooper.filter("timestamp", function (jsonValue) {
  return function (input) {
    var date = input;

    //  ...
    //  Posted 23 hours ago
    //Posted 1 day ago
    //Posted x days ago
    //  ...
    //  Posted 7 days ago
    //Posted on DD/MM/YYYY HH:MM

    var duration = Math.abs(moment(date).diff(moment(), "days"));
    if (duration > 7) {
      return {translate: moment(date, "DD/MM/YYYY h:mm"), number: duration};
    }
    else if (duration > 1 && duration <= 7) {
      return {translate: "xDaysAgo", number: duration}
    }
    else if (duration == 1) {
      return {translate: "1DayAgo", number: duration}
    }

    //  Posted 59 mins ago
    //Posted 1 hour ago
    //Posted x hours ago

    duration = Math.abs(moment(date).diff(moment(), "hours"));
    if (duration > 1) {
      return {translate: "xHoursAgo", number: duration}
    }
    else if (duration == 1) {
      return {translate: "1HourAgo", number: duration}
    }

    //Just now
    //Posted 1 min ago
    //Posted x mins ago

    duration = Math.abs(moment(date).diff(moment(), "minutes"));
    if (duration > 1) {
      return {translate: "xMinutesAgo", number: duration}
    }
    else if (duration == 1) {
      return {translate: "1MinuteAgo", number: duration}
    }

    return "justNow";
  };
});