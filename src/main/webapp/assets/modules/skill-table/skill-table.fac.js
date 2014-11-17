angular.module('Skill').factory('skillTableFactory', function (jsonValue, utils) {
  var instance = {
    renderView: function () {
      var lCur = Date.today().toString("MMM d"),
        fCur = Date.today().add(-7).days().toString("MMM d"),
        current = fCur + ' - ' + lCur;

      var lPre = Date.today().add(-8).days().toString("MMM d"),
        fPre = Date.today().add(-7).days().clone(),
        previous = fPre.add(-7).days().toString("MMM d") + ' - ' + lPre;

      $('span.curDate').text(current);
      $('span.preDate').text(previous);
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
    //
    //reformatData: function (data) {
    //  var newData = [];
    //  var icStock = '';
    //  $.each(data, function (index, value) {
    //    var per = (value.previousCount !== 0 ? (value.currentCount - value.previousCount) / value.previousCount
    //        : value.currentCount - value.previousCount) * 100;
    //    if (per > 0) {
    //      icStock = 'fa-arrow-up ic-blue';
    //      per = per.toFixed(2).replace(/(\.[0-9]*?)0+$/, "$1").replace(/\.$/, "");
    //    }
    //    else if (per < 0) {
    //      icStock = 'fa-arrow-down ic-red';
    //      per = per.toFixed(2).replace(/(\.[0-9]*?)0+$/, "$1").replace(/\.$/, "");
    //    }
    //    else {
    //      icStock = '';
    //      per = 0;
    //    }
    //    newData.push({
    //      'name': value.skillName,
    //      'current': value.currentCount,
    //      'previous': value.previousCount,
    //      'change': per,
    //      'icon': icStock
    //    });
    //  });
    //  return newData;
    //},

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