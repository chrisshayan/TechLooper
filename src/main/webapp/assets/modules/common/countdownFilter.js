techlooper.filter("countdown", function(jsonValue) {
  return function(input, type) {
    if (!input) return "";
    type = type || "day";
    switch (type) {
      case "day":
        var toNow = moment(input, jsonValue.dateFormat).diff(moment(), "days");
        return toNow + 1;
    }
  }
});