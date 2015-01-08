angular.module("Common").factory("cleanupFactory", function (jsonValue, utils, localStorageService, $rootScope) {

  var $$ = {
    cleanHighCharts: function () {
      $.each(Highcharts.charts, function (i, chart) {
        chart !== undefined && chart.destroy();
        return true;
      });
      Highcharts.charts.length = 0;
    },

    cleanSession: function () {
      $rootScope.userInfo = undefined;
      localStorageService.cookie.remove(jsonValue.storage.key);
    }
  }

  var instance = {
    initialize: function () {}
  };

  utils.registerNotification(jsonValue.notifications.switchScope, $$.cleanHighCharts);
  utils.registerNotification(jsonValue.notifications.loginFailed, $$.cleanSession);
  utils.registerNotification(jsonValue.notifications.logoutSuccess, $$.cleanSession);
  return instance;
});