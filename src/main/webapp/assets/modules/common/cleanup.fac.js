angular.module("Common").factory("cleanupFactory", function (jsonValue, utils, localStorageService) {

  var $$ = {
    cleanHighCharts: function () {
      $.each(Highcharts.charts, function (i, chart) {
        chart !== undefined && chart.destroy();
        return true;
      });
      Highcharts.charts.length = 0;
    }
  }

  var instance = {
    initialize: function () {},

    cleanSession: function () {
      localStorageService.remove(jsonValue.storage.key);
    }
  };

  utils.registerNotification(jsonValue.notifications.switchScope, $$.cleanHighCharts);
  utils.registerNotification(jsonValue.notifications.loginFailed, instance.cleanSession);
  return instance;
});