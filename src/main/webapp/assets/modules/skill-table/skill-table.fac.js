angular.module('Skill').factory('skillTableFactory', function (jsonValue, utils) {
  var instance = {
    renderView: function (viewJson) {
      var oneSkill = viewJson.tableAndChartJson[0];
      var currentPeriod = Date.today();
      var previousPeriod = Date.today().last().week();
      var doublePreviousPeriod = Date.today().last().week().last().week();

      switch (oneSkill.preAndCurrCountPeriod) {
          case "month":
              previousPeriod = Date.today().last().month();
              doublePreviousPeriod = Date.today().last().month().last().month();
              break;
          case "quarter":
              previousPeriod = Date.today().last().quarter();
              doublePreviousPeriod = previousPeriod.last().quarter().last().quarter();
              break;
      }

      $('span.curDate').text(previousPeriod.toString("MMM d") + ' - ' + currentPeriod.toString("MMM d"));
      $('span.preDate').text(doublePreviousPeriod.toString("MMM d") + ' - ' + previousPeriod.toString("MMM d"));
    },

    calculatePercentage: function(viewJson) {
      var tableAndChartJson = viewJson.tableAndChartJson;
      var icStock = '';
      $.each(tableAndChartJson, function(i, item) {
        var percent = (item.currentCount - item.previousCount) / Math.max(item.previousCount, 1) * 100;
        // TODO find way to remove if
        if (percent > 0) {
          icStock = 'fa-arrow-up ic-blue';
          percent = percent.toFixed(2).replace(/(\.[0-9]*?)0+$/, "$1").replace(/\.$/, "");
        }
        else if (percent < 0) {
          icStock = 'fa-arrow-down ic-red';
          percent = percent.toFixed(2).replace(/(\.[0-9]*?)0+$/, "$1").replace(/\.$/, "");
        }
        else {
          icStock = '';
          percent = 0;
        }
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