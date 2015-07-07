techlooper.filter("countdown", function(jsonValue) {
  return function(input, type) {
    if (!input) return "";
    type = type || "day";
    switch (type) {
      case "day":
        var toNow = moment.utc(moment(input, jsonValue.dateFormat).diff(moment())).format("D");
        return toNow + 1;
    }
  }
});