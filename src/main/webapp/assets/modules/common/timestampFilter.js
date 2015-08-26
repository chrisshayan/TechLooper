techlooper.filter("timestamp", function (jsonValue) {
  return function (input, type) {
    var date = input;

    switch (type) {
      case 'hour':
        return moment(input, jsonValue.dateTimeFormat).format('h:mm A');

      case 'shortDate':
        return moment(input, jsonValue.dateFormat).format('ddd, DD MMMM');
    }

    var duration = Math.abs(moment(date).diff(moment(), "days"));
    if (duration > 7) {
      return {translate: moment(date).format("DD/MM/YYYY h:mm A")};
    }
    else if (duration > 1 && duration <= 7) {
      return {translate: "xDaysAgo", number: duration}
    }
    else if (duration == 1) {
      return {translate: "1DayAgo"}
    }

    //  Posted 59 mins ago
    //Posted 1 hour ago
    //Posted x hours ago

    duration = Math.abs(moment(date).diff(moment(), "hours"));
    if (duration > 1) {
      return {translate: "xHoursAgo", number: duration}
    }
    else if (duration == 1) {
      return {translate: "1HourAgo"}
    }

    //Just now
    //Posted 1 min ago
    //Posted x mins ago

    duration = Math.abs(moment(date).diff(moment(), "minutes"));
    if (duration > 1) {
      return {translate: "xMinutesAgo", number: duration}
    }
    else if (duration == 1) {
      return {translate: "1MinuteAgo"}
    }

    return {translate:"justNow"};
  };
});