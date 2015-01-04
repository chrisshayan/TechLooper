angular.module("Chart").factory("chartService", function (utils, jsonValue, pieFactory, bubbleFactory) {
  var instance = {
    getChartFactory: function () {
      switch (utils.getView()) {
        case jsonValue.views.pieChart:
          return pieFactory;

        case jsonValue.views.bubbleChart:
          return bubbleFactory;
      }
    }
  }

  return instance;
});
