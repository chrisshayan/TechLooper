$.expr[":"].containsIgnoreCase = $.expr.createPseudo(function (arg) {
  return function (elem) {
    return jQuery(elem).text().toLowerCase() === arg.toLowerCase();
  };
});

$.extend({
  distinct: function (array) {
    var result = [];
    $.each(array, function (i, v) {
      if ($.inArray(v, result) == -1) result.push(v);
    });
    return result;
  }
});

Array.prototype.min = function () {
  return this.reduce(function (x, y) {
    return ( x < y ? x : y );
  });
}

Array.prototype.max = function () {
  return this.reduce(function (x, y) {
    return ( x > y ? x : y );
  });
}

Array.prototype.minBy = function (prop) {
  var values = [];
  $.each(this, function(i, v){values.push(v[prop]);});
  return values.min();
}

Array.prototype.maxBy = function (prop) {
  var values = [];
  $.each(this, function(i, v){values.push(v[prop]);});
  return values.max();
}

Array.prototype.toArray = function (prop) {
  var values = [];
  $.each(this, function(i, v){values.push(v[prop]);});
  return values;
}
