angular.module("Skill").factory("skillAnalyticsService", function (jsonValue, utils, skillTableFactory, skillChartFactory) {

  var $$ = {
    highLight: function(skillName) {
      skillTableFactory.highLightRow(skillName);
      skillChartFactory.highLight(skillName);
    }
  }

  var instance =  {
    getCirclesJson: function(viewJson) {
      var skills = utils.getTopItems(viewJson.skills, ["currentCount"], 10);
      var colorIndex = -1;
      $.each(skills, function(index, skill) {
        colorIndex = (index >= jsonValue.skillColors.length) ? 0 : colorIndex + 1;
        skill.color = jsonValue.skillColors[colorIndex];
      });
      return skills;
    },

    getTableAndChartJson: function(viewJson, mergeItems) {
      var topItems = utils.getTopItems(viewJson.skills, ["previousCount", "currentCount"], 3);
      topItems = $.merge(mergeItems, utils.getNonZeroItems(topItems, ["previousCount", "currentCount"]));
      return $.unique(topItems);
    },

    map: function(rawJson) {
      var viewJson = $.extend(true, {}, rawJson);
      var skills = viewJson.skills;
      $.each(skills, function(index, skill) {
        var twoWeek = jsonPath.eval(skill, "$.histograms[?(@.name=='" + jsonValue.histograms.twoWeeks + "')].values")[0];
        skill.previousCount = twoWeek[0];
        skill.currentCount = twoWeek[1];
        skill.histogramData = jsonPath.eval(skill, "$.histograms[?(@.name=='" + jsonValue.histograms.thirtyDays + "')].values")[0];
        delete skill.histograms;
      });
      return viewJson;
    },

    registerEvents: function() {
      skillTableFactory.registerEvents();
    }
  }

  utils.registerNotification(jsonValue.notifications.mouseHover, $$.highLight, function(){return $("div.technical-detail-page").is(":visible");});

  return instance;
});