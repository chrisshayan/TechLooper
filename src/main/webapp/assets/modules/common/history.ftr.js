angular.module("Common").factory("historyFactory", function (jsonValue, $location, $rootScope) {
  var historyStack = {max: 0, items: {}};

  $rootScope.$on('$routeChangeSuccess', function() {
    switch ($location.path()) {
      case jsonValue.routerUris.bubble:
      case jsonValue.routerUris.pie:
        $("body").css("background-color", "#2e272a");
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