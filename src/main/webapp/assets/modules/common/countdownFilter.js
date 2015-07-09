techlooper.filter("countdown", function (jsonValue) {
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
    }
  }
});