angular.module("Common").factory("historyFactory", function (jsonValue, $location, $rootScope, utils) {
  var historyStack = {max: 0, items: {}};

  $rootScope.$on('$routeChangeSuccess', function(event, route) {
    switch (utils.getView()) {
      case jsonValue.views.bubbleChart:
      case jsonValue.views.pieChart:
        // TODO: #1 - change the body background to black
        $("body").css("background-color", "#201d1e");
        instance.trackHistory();
        break;
    }
  });

  var instance = {
    trackHistory: function() {
      historyStack.items[$location.path()] = ++historyStack.max;
    },

    popHistory: function() {
      if (historyStack.max === 0) return undefined;
      for (var path in historyStack.items) {
        if (historyStack.items[path] === historyStack.max) {
          return path;
        }
      }
      return undefined;
    }
  }

  return instance;
});