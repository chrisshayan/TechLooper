angular.module("Common").factory("shortcutFactory", function (jsonValue, $location, $rootScope, historyFactory) {
  var view = function (path) {
    if (/\/jobs\/search\//i.test(path)) {
      return jsonValue.views.jobsSearch;
    }
    else if (/\/jobs\/search/i.test(path)) {
      return jsonValue.views.jobsSearchText;
    }
  }

  var traps = {
    esc: function (e) {
      switch (view($location.path())) {
        case jsonValue.views.jobsSearch:
          if ($("#companyVideoInfor").is(":visible")) {// ESC from others, such as: Video dialog, ...
            return;
          }
          $location.path(jsonValue.routerUris.jobsSearch);
          $rootScope.$apply();
          break;
        case jsonValue.views.jobsSearchText:
          var path = historyFactory.popHistory();
          $location.path(path === undefined ? "/" : path);
          $rootScope.$apply();
          break;
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
    trigger: function (key) {
      Mousetrap.trigger(key);
    }
  };
});