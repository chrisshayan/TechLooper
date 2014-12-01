angular.module("Common").factory("cleanupFactory", function (jsonValue, utils) {

  var $$ = {
    cleanHighCharts: function () {
      $.each(Highcharts.charts, function (i, chart) {
        chart !== undefined && chart.destroy();
        return true;
      });
      Highcharts.charts.length = 0;
    }
  }
  utils.registerNotification(jsonValue.notifications.switchScope, $$.cleanHighCharts);
  return {
    initialize: function () {}
  };
});