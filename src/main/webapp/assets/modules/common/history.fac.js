angular.module("Common").factory("historyFactory", function (jsonValue, $location, $rootScope, utils) {
  var historyStack = [];

  $rootScope.$on('$routeChangeSuccess', function (event, next, current) {
    switch (utils.getView()) {
      case jsonValue.views.analyticsSkill://dont need to keep track this url
      case jsonValue.views.signIn://dont need to keep track this url
        break;

      case jsonValue.views.bubbleChart:
      case jsonValue.views.pieChart:
        // TODO: #1 - change the body background to black
        $("body").css("background-color", "#201d1e")

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
      historyStack.pop(); // remove current item
      if (historyStack.length === 0) return "/";
      return historyStack.pop();
    }
  }
  return instance;
});