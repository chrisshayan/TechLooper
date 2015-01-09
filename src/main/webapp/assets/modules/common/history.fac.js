angular.module("Common").factory("historyFactory", function (jsonValue, $location, $rootScope, utils) {
  var historyStack = [];
  var exceptViews = [jsonValue.views.analyticsSkill, jsonValue.views.signIn];

  $rootScope.$on('$routeChangeSuccess', function (event, next, current) {
    var view = utils.getView();
    switch (view) {
      case jsonValue.views.bubbleChart:
      case jsonValue.views.pieChart:
        // TODO: #1 - change the body background to black
        $("body").css("background-color", "#201d1e");

      default:
        instance.trackHistory();
    }

    utils.sendNotification(jsonValue.notifications.changeUrl/*, current.$$route.originalPath, next.$$route.originalPath*/);
  });

  var instance = {
    initialize: function () {},

    trackHistory: function () {
      while (historyStack.indexOf($location.path()) >= 0) {historyStack.pop();}
      historyStack.push($location.path());
    },

    popHistory: function () {
      console.log(historyStack);
      var url; // remove current item
      do {url = historyStack.pop()} while(historyStack.indexOf(url) >= 0)
      if (historyStack.length === 0) return "/";
      return historyStack.pop();
    }
  }
  return instance;
});