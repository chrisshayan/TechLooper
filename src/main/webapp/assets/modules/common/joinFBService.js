techlooper.factory("joinFBService", function (jsonValue, $rootScope, utils, $location, $routeParams, localStorageService,
                                              apiService) {
  //$rootScope.$on("$routeChangeSuccess", function (event, next, current) {
  //  var view = utils.getView();
  //  switch (view) {
  //    case jsonValue.views.challengeDetail:
  //      var parts = $routeParams.id.split("-");
  //      var lastPart = parts.pop();
  //      if (parts.length < 2 || (lastPart !== "id")) {
  //        return $location.path("/");
  //      }
  //
  //      var id = parts.pop();
  //      var title = parts.join("");
  //      if (utils.hasNonAsciiChar(title)) {
  //        title = utils.toAscii(title);
  //        return $location.url(sprintf(jsonValue.routerUris[view] + "/%s-%s-id", title, id));
  //      }
  //  }
  //
  //  return true;
  //});

  return {
    initialize: function () {}
  }

});