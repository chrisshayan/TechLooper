angular.module("Skill").factory("skillAnalyticsService",
  function (jsonValue, utils, skillTableFactory, skillChartFactory, shortcutFactory, $location) {
    var viewJson;
    var scope;
    var skillStatisticRequest;
    var currentHeight = 0;

    var $$ = {
      initialize: function($scope) {
        scope = $scope;
      },

      extractCirclesJson: function () {
        var skills = utils.getTopItems(viewJson.skills, ["currentCount"], 10);
        var colorIndex = -1;
        $.each(skills, function (index, skill) {
          colorIndex = (index >= jsonValue.skillColors.length) ? 0 : colorIndex + 1;
          skill.color = jsonValue.skillColors[colorIndex];
        });
        viewJson.circles = skills;
        return viewJson.circles;
      },

      extractTableAndChartJson: function (mergeItems) {
        var topItems = utils.getTopItems(viewJson.skills, ["previousCount", "currentCount"], 3);
        var nonZeroMergeItems = utils.getNonZeroItems(mergeItems, ["previousCount", "currentCount"]);
        topItems = $.merge(nonZeroMergeItems,
          utils.getNonZeroItems(topItems, ["previousCount", "currentCount"]));
        viewJson.tableAndChartJson = $.distinct(topItems);
        return viewJson.tableAndChartJson;
      },

      map: function (rawJson) {
        viewJson = $.extend(true, {}, rawJson);
        //TODO collect data base on period: 2w, 2m, 2quarters...
        $.each(viewJson.skills, function (index, skill) {
          var histograms = skillStatisticRequest.histograms;
          var prevAndCurr = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[0] + "')].values")[0];
          skill.previousCount = prevAndCurr[0];
          skill.currentCount = prevAndCurr[1];
          skill.histogramData = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[1] + "')].values")[0];
          delete skill.histograms;
        });
        return viewJson;
      },

      highLight: function (skillName) {
        skillTableFactory.highLightRow(skillName);
        skillChartFactory.highLight(skillName);
      },

      setActiveChartType: function () {
        var type = $('.chart-management ul').find('li');
        type.on('click', function () {
          $location.path(jsonValue.routerUris.analyticsSkill + "/" + skillStatisticRequest.term + "/" + $(this).data("period"));
          scope.$apply();
          $('.technical-detail-page').css('height',currentHeight);
        });
      },

      renderView: function() {
        switch (skillStatisticRequest.period) {
          case "month":
            $("li[data-period=month]").addClass("active").find('i').addClass('fa-dot-circle-o');
            break;
          case "quarter":
            $("li[data-period=quarter]").addClass("active").find('i').addClass('fa-dot-circle-o');
            break;
          default:
            $("li[data-period=week]").addClass("active").find('i').addClass('fa-dot-circle-o');
            break;
        }
      }
    }

    var instance = {

      getHistograms: function (period) {
        var histograms = [jsonValue.histograms.twoWeeks, jsonValue.histograms.thirtyDays];
        switch (period) {
          case "month":
            histograms = [jsonValue.histograms.twoMonths, jsonValue.histograms.lineChart];
            break;
          case "quarter":
            histograms = [jsonValue.histograms.twoQuarters, jsonValue.histograms.thirtyDays]
            break;
        }
        return histograms;
      },

      extractViewJson: function (termJson, $skillStatisticRequest) {
        skillStatisticRequest = $skillStatisticRequest;
        $$.map(termJson);
        $$.extractTableAndChartJson($$.extractCirclesJson());
        $$.setActiveChartType();
        $$.renderView();
        return viewJson;
      },

      registerEvents: function () {
        skillTableFactory.registerEvents();
        $('.btn-close').click(function () {
          shortcutFactory.trigger('esc');
        });
        $('.btn-logo').click(function () {
          shortcutFactory.trigger('esc');
        });
        currentHeight = $('.technical-detail-page').height();
      },

      getViewJson: function () {
        return viewJson;
      }
    }

    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, function () {return $("div.technical-detail-page").is(":visible");});
    utils.registerNotification(jsonValue.notifications.mouseHover, $$.highLight, function () {return $("div.technical-detail-page").is(":visible");});

    return instance;
  });