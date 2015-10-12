techlooper.filter("total", function (jsonValue, $filter) {
  return function (array, property) {
    if (!array) return 0;
    var vals = array.map(function (obj) {
      if (obj[property]) return obj[property];
      return 0
    });
    var rs = eval(vals.join("+"));
    return rs || 0;
  }
});