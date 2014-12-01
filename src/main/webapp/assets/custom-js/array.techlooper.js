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
  $.each(this, function (i, v) {values.push(v[prop]);});
  return values.min();
}

Array.prototype.maxBy = function (prop) {
  var values = [];
  $.each(this, function (i, v) {values.push(v[prop]);});
  return values.max();
}

Array.prototype.toArray = function (prop) {
  var values = [];
  $.each(this, function (i, v) {values.push(v[prop]);});
  return values;
}

Array.prototype.shuffle = function () {
  var m = this.length, t, i;
  while (m) {
    i = Math.floor(Math.random() * m--);
    t = this[m];
    this[m] = this[i];
    this[i] = t;
  }
  return this;
}

Array.prototype.distinct = function () {
  var result = [];
  $.each(this, function (i, v) {
    if ($.inArray(v, result) == -1) result.push(v);
  });
  return result;
}

Array.prototype.findFirst = function (val, prop) {
  var index = undefined;
  $.each(this, function (i, v) {
    var value = (prop === undefined ? v : v[prop] );
    if (value === val) {
      index = i;
      return false;
    }
  });
  return this[index];
}
