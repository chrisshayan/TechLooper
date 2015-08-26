techlooper.factory("seoService", function ($rootScope, utils, $location) {

  $rootScope.$on("$routeChangeStart", function (event, next, current) {
    var uiView = utils.getUiView();
    switch (uiView.type) {
      case "SEO":
        if (!next || !next.params) break;
        var parts = next.params.id.split("-");
        var lastPart = parts.pop();
        if (parts.length < 2 || (lastPart !== "id")) {
          return $location.path("/");
        }

        var id = parts.pop();
        var title = parts.join("-");
        if (utils.hasNonAsciiChar(title)) {
          title = utils.toAscii(title);
          event.preventDefault();
          return $location.url(sprintf("%s/%s-%s-id", uiView.url, title, id));
        }
    }
  });

  return {
    initialize: function () {}
  }
});