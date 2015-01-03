angular.module("Common").factory("historyFactory", function (jsonValue, $location, $rootScope, utils) {
  var historyStack = [];
  //var historyStack = {max: 0, items: {}};

  $rootScope.$on('$routeChangeSuccess', function (event, next, current) {
    switch (utils.getView()) {
      case jsonValue.views.bubbleChart:
      case jsonValue.views.pieChart:
        // TODO: #1 - change the body background to black
        $("body").css("background-color", "#201d1e");
        //instance.trackHistory();
        break;
    }
    instance.trackHistory();
    //next.$$route.originalPath;
    //current.$$route.originalPath;
    utils.sendNotification(jsonValue.notifications.changeUrl/*, current.$$route.originalPath, next.$$route.originalPath*/);
  });

  var instance = {
    initialize: function () {},

    trackHistory: function () {
      while (historyStack.indexOf($location.path()) >= 0) {historyStack.pop();}
      historyStack.push($location.path());
      //historyStack.items[$location.path()] = ++historyStack.max;
    },

    popHistory: function () {
      historyStack.pop(); // remove current item
      if (historyStack.length === 0) return "/";
      //if (historyStack.max === 0) return undefined;
      //for (var path in historyStack.items) {
      //  if (historyStack.items[path] === historyStack.max) {
      //    return path;
      //  }
      //}
      return historyStack.pop();
    }
  }
  return instance;
});