techlooper.filter("nonAscii", function (utils) {
  return function (input) {
    if (utils.hasNonAsciiChar(input)) {
      input = utils.toAscii(input);
    }
    return input;
  }
});