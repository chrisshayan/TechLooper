$.expr[":"].containsIgnoreCase = $.expr.createPseudo(function (arg) {
  return function (elem) {
    return $(elem).text().toLowerCase() === arg.toLowerCase();
  };
});
