angular.module("Skill").factory("skillAnalyticsService", function (jsonValue, utils, skillTableFactory, skillChartFactory, shortcutFactory) {
  var viewJson;

  var $$ = {
    extractCirclesJson: function() {
      var skills = utils.getTopItems(viewJson.skills, ["currentCount"], 10);
      var colorIndex = -1;
      $.each(skills, function(index, skill) {
        colorIndex = (index >= jsonValue.skillColors.length) ? 0 : colorIndex + 1;
        skill.color = jsonValue.skillColors[colorIndex];
      });
      viewJson.circles = skills;
      return viewJson.circles;
    },

    extractTableAndChartJson: function(mergeItems) {
      var topItems = utils.getTopItems(viewJson.skills, ["previousCount", "currentCount"], 3);
      var nonZeroMergeItems = utils.getNonZeroItems(mergeItems, ["previousCount", "currentCount"]);
      topItems = $.merge(nonZeroMergeItems,
        utils.getNonZeroItems(topItems, ["previousCount", "currentCount"]));
      viewJson.tableAndChartJson = $.distinct(topItems);
      return viewJson.tableAndChartJson;
    },

    map: function(rawJson, skillStatisticRequest) {
      viewJson = $.extend(true, {}, rawJson);
      //TODO collect data base on period: 2w, 2m, 2quarters...
      $.each(viewJson.skills, function(index, skill) {
        var histograms = skillStatisticRequest.histograms;
        var prevAndCurr = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[0] + "')].values")[0];
        skill.previousCount = prevAndCurr[0];
        skill.currentCount = prevAndCurr[1];
        skill.histogramData = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[1] + "')].values")[0];
        delete skill.histograms;
      });
      return viewJson;
    },

    highLight: function(skillName) {
      skillTableFactory.highLightRow(skillName);
      skillChartFactory.highLight(skillName);
    },

    setActiveChartType: function(){
      var type = $('.chart-management ul').find('li');
      type.on('click', function(){
        type.removeClass('active').find('i').removeClass('fa-dot-circle-o');
        if(!$(this).hasClass('active')){
          $(this).addClass('active').find('i').addClass('fa-dot-circle-o');
        }
      });
    }
  }

  var instance =  {
    getHistograms: function(period) {
      var histograms = [jsonValue.histograms.twoWeeks, jsonValue.histograms.thirtyDays];
      switch (period) {
        case "month":
          break;
        case "quarter":
          break;
      }
      return histograms;
    },

    extractViewJson: function(termJson, skillStatisticRequest) {
      $$.map(termJson, skillStatisticRequest);
      $$.extractTableAndChartJson($$.extractCirclesJson());
      $$.setActiveChartType();
      return viewJson;
    },

    registerEvents: function() {
      skillTableFactory.registerEvents();
      $('.btn-close').click(function() {
        shortcutFactory.trigger('esc');
      });
      $('.btn-logo').click(function() {
        shortcutFactory.trigger('esc');
      });
    },

    getViewJson: function() {
      return viewJson;
    }
  }

  utils.registerNotification(jsonValue.notifications.mouseHover, $$.highLight, function(){return $("div.technical-detail-page").is(":visible");});

  return instance;
});