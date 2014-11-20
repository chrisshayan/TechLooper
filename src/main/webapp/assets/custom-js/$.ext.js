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