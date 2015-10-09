techlooper.filter("total", function (jsonValue, $filter) {
  return function (array, property) {
    if (!array) return 0;
    var vals = array.map(function (obj) {return obj[property];});
    var rs = eval(vals.join("+"));
    return rs || 0;
  }
});