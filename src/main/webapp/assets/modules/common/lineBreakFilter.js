techlooper.filter("lineBreak", function () {
  return function (text) {
    if (text != undefined) return text.replace(/\n/g, "<br />");
  };
});