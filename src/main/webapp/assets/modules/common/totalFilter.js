techlooper.filter("total", function (jsonValue, $filter) {
  return function (array, property) {
    if (!array) return "";
    var vals = array.map(function (obj) {return obj[property];});
    return eval(vals.join("+"));
  }
});