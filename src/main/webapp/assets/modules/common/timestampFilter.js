techlooper.filter("timestamp", function (jsonValue) {
  return function (input) {
    var date = input;

    //Just now
    //Posted 1 min ago
    //Posted x mins ago
    //  ...
    //  Posted 59 mins ago
    //Posted 1 hour ago
    //Posted x hours ago
    //  ...
    //  Posted 23 hours ago
    //Posted 1 day ago
    //Posted x days ago
    //  ...
    //  Posted 7 days ago
    //Posted on DD/MM/YYYY HH:MM

    var duration = Math.abs(moment(date).diff(moment(), "days"));



    //if (mins < 1) {
    //  return "justNow";
    //}
    //else if (mins < 2) {
    //  return "1minAgo";
    //}
    //else if (mins >= 2) {
    //  return "minsAgo";
    //}

    return "abc";
  };
});