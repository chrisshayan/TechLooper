angular.module("Chart").factory("chartService", function (utils, jsonValue, pieFactory) {
  var instance = {
    getChartFactory: function () {
      switch (utils.getView()) {
        case jsonValue.views.pieChart:
          return pieFactory;
      }
    }
  }

  return instance;
});
