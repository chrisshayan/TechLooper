techlooper.filter("textTruncate", function () {
  return function (text, type) {
    if (!text) return "";

    switch (type) {
      case "email":
        return text.replace(/@.*/, "");

      case "display-text":
        return text.split(" ")[0];

      case "capitalize":
        return text.capitalize();

      case "lowercase":
        return text.toLowerCase();
    }
    return text;
  };
});