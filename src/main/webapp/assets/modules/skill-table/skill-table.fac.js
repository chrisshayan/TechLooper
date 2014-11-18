angular.module('Skill').factory('skillTableFactory', function (jsonValue, utils) {
  var instance = {
    renderView: function (viewJson) {
      var period = viewJson.tableAndChartJson[0].preAndCurrCountPeriod;
      var from = utils.getDatePeriods(2, period).ago().toString("MMM d");
      var to = (0).days().fromNow().toString("MMM d");
      var between = utils.getDatePeriods(1, period).ago().toString("MMM d");
      $('span.curDate').text([between, to].join(" - "));
      $('span.preDate').text([from, between].join(" - "));
    },

    calculatePercentage: function (viewJson) {
      var tableAndChartJson = viewJson.tableAndChartJson;
      var icStock = '';
      $.each(tableAndChartJson, function (i, item) {
        var percent = (item.currentCount - item.previousCount) / Math.max(item.previousCount, 1) * 100;
        percent = percent !== 0  && (percent.toFixed(2) / percent) !== 1 ? percent.toFixed(2) : percent;
        icStock = percent > 0 ? 'fa-arrow-up ic-blue' : (percent < 0 ? 'fa-arrow-down ic-red' : '');
        item.percent = percent;
        item.icon = icStock;
      });
    },

    highLightRow: function (skillName) {
      var oj = $('.rwd-table').find('tr');
      oj.removeClass('active');
      oj.each(function () {
        if ($(this).find('td[data-th=Skill]').text() == skillName) {
          $(this).addClass('active');
        }
      });
    },

    registerEvents: function () {
      $("tr.skillItem").on('click mouseover', function () {
        utils.sendNotification(jsonValue.notifications.mouseHover, $(this).find("td:first").text());
      });
    }
  };

  return instance;
});