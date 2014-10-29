angular.module("Common").factory("shortcutFactory", function (jsonValue, $location, $rootScope, historyFactory) {
  var traps = {
    esc: function (e) {
      var path = $location.path();
      if (/\/jobs\/search\//i.test(path)) {
        if ($("#companyVideoInfor").is(":visible")) {// ESC from others, such as: Video dialog, ...
          return;
        }
        $location.path(jsonValue.routerUris.jobsSearch);
        $rootScope.$apply();
      }
      else if (/\/jobs\/search/i.test(path)) {
        var path = historyFactory.popHistory();
        $location.path(path === undefined ? "/" : path);
        $rootScope.$apply();
      }
    }
  }

  Mousetrap.bindGlobal("esc", function (e) {
    if (e.defaultPrevented) {
      return;
    }
    traps.esc(e);
  });

  return {
    trigger: function(key) {
      Mousetrap.trigger(key);
    }
  };
});