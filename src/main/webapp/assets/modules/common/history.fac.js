angular.module("Common").factory("historyFactory", function (jsonValue, $location, $rootScope, utils) {
  var historyStack = [];
  var exceptViews = [jsonValue.views.analyticsSkill, jsonValue.views.signIn];

  $rootScope.$on('$routeChangeSuccess', function (event, next, current) {
    switch (utils.getView()) {
      case jsonValue.views.bubbleChart:
      case jsonValue.views.pieChart:
        // TODO: #1 - change the body background to black
        $("body").css("background-color", "#201d1e");
    }
    instance.trackHistory();
    utils.sendNotification(jsonValue.notifications.changeUrl/*, current.$$route.originalPath, next.$$route.originalPath*/);
  });

  var instance = {
    initialize: function () {},

    trackHistory: function () {
      var path = $location.path();
      while (historyStack.indexOf(path) >= 0) {historyStack.pop();}
      historyStack.push(path);
    },

    popHistory: function () {
      if (historyStack.length === 0) {
        return undefined;
      }

      var url = historyStack.pop(), view; // remove current item
      do {
        url = historyStack.pop();
        view = utils.getView(url);
      }
      while (exceptViews.indexOf(view) >= 0 && historyStack.length > 0);
      //if (historyStack.length === 0) return "/";
      //return historyStack.pop();
      switch (utils.getView()) {
        case jsonValue.views.jobsSearchText:
          return jsonValue.routerUris.jobsSearch;

        default:
          return url;
      }
    }
  }
  return instance;
});