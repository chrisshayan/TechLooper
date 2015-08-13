techlooper.filter("textTruncate", function () {
  return function (text, type) {
    if (!text) return "";

    switch (type) {
      case "email":
        return text.replace(/@.*/, "");
    }
    return text;
  };
});