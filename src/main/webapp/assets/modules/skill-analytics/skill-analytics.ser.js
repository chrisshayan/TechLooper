angular.module("Skill").factory("skillAnalyticsService",
  function (jsonValue, utils, skillTableFactory, skillChartFactory, shortcutFactory, $location) {
    var viewJson;
    var scope;
    var skillStatisticRequest;

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
        $.each(viewJson.skills, function (index, skill) {
          var histograms = skillStatisticRequest.histograms;
          var prevAndCurr = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[0] + "')].values")[0];
          skill.previousCount = prevAndCurr[0];
          skill.currentCount = prevAndCurr[1];
          skill.histogramData = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[1] + "')].values")[0];
          skill.histogramDataPeriod = utils.getHistogramPeriod(histograms[1]);
          skill.preAndCurrCountPeriod = skillStatisticRequest.period;
          delete skill.histograms;
        });
        return viewJson;
      },

      highLight: function (skillName) {
        skillTableFactory.highLightRow(skillName);
        skillChartFactory.highLight(skillName);
      },

      renderPeriodRadios: function () {
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
      },

      renderView: function() {
        $$.renderPeriodRadios();
      }
    }

    var instance = {

      getHistogramsAndPeriod: function (period) {
        var histogramsAndPeriod = {
          histograms: [jsonValue.histograms.twoWeeks, jsonValue.histograms.oneWeek],
          period: period
        };
        switch (period) {
          case "month":
            histogramsAndPeriod.histograms = [jsonValue.histograms.twoMonths, jsonValue.histograms.oneMonth];
            break;
          case "quarter":
            histogramsAndPeriod.histograms = [jsonValue.histograms.twoQuarters, jsonValue.histograms.eighteenBlocksOfFiveDays]
            break;
          default:
            histogramsAndPeriod.period = "week"
        }
        return histogramsAndPeriod;
      },

      extractViewJson: function (termJson, $skillStatisticRequest) {
        skillStatisticRequest = $skillStatisticRequest;
        $$.map(termJson);
        $$.extractTableAndChartJson($$.extractCirclesJson());
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

        var type = $('.chart-management ul').find('li');
        type.unbind("click");
        type.bind('click', function () {
          $location.path(jsonValue.routerUris.analyticsSkill + "/" + skillStatisticRequest.term + "/" + $(this).data("period"));
          scope.$apply();
        });
      },

      getViewJson: function () {
        return viewJson;
      }
    }

    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, function () {return $("div.technical-detail-page").is(":visible");});
    utils.registerNotification(jsonValue.notifications.mouseHover, $$.highLight, function () {return $("div.technical-detail-page").is(":visible");});

    return instance;
  });