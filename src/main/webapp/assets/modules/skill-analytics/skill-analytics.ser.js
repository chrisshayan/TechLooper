angular.module("Skill").factory("skillAnalyticsService", function (jsonValue, utils, skillTableFactory, skillChartFactory) {
  var viewJson = undefined;

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

    map: function(rawJson) {
      viewJson = $.extend(true, {}, rawJson);
      $.each(viewJson.skills, function(index, skill) {
        var twoWeek = jsonPath.eval(skill, "$.histograms[?(@.name=='" + jsonValue.histograms.twoWeeks + "')].values")[0];
        skill.previousCount = twoWeek[0];
        skill.currentCount = twoWeek[1];
        skill.histogramData = jsonPath.eval(skill, "$.histograms[?(@.name=='" + jsonValue.histograms.thirtyDays + "')].values")[0];
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
    extractViewJson: function(termJson) {
      $$.map(termJson);
      $$.extractTableAndChartJson($$.extractCirclesJson());
      return viewJson;
    },

    registerEvents: function() {
      skillTableFactory.registerEvents();
    },

    getViewJson: function() {
      return viewJson;
    },
    chartManagement: function(){
      $$.setActiveChartType();
    }
  }

  utils.registerNotification(jsonValue.notifications.mouseHover, $$.highLight, function(){return $("div.technical-detail-page").is(":visible");});

  return instance;
});