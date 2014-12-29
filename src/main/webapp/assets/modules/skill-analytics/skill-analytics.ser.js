angular.module("Skill").factory("skillAnalyticsService",
  function (jsonValue, utils, skillTableFactory, skillChartFactory, shortcutFactory, $location, termService) {
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
        viewJson.tableAndChartJson = topItems.distinct();
        return viewJson.tableAndChartJson;
      },

      toViewJson: function (rawJson) {
        //viewJson = $.extend({}, {}, rawJson)
        viewJson = termService.toViewTerm({term: skillStatisticRequest.term});
        viewJson = $.extend({}, viewJson, skillStatisticRequest);
        viewJson = $.extend({}, viewJson, rawJson);
        var histograms = skillStatisticRequest.histograms;
        viewJson = $.extend({}, viewJson, {histogramDataPeriod: utils.getHistogramPeriod(histograms[1])})
        $.each(viewJson.skills, function (index, skill) {
          var prevAndCurr = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[0] + "')].values")[0];
          skill.previousCount = prevAndCurr[0];
          skill.currentCount = prevAndCurr[1];
          skill.histogramData = jsonPath.eval(skill, "$.histograms[?(@.name=='" + histograms[1] + "')].values")[0];
          //skill.histogramDataPeriod = utils.getHistogramPeriod(histograms[1]);
          //skill.preAndCurrCountPeriod = skillStatisticRequest.period;
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
          case "sixMonths":
            $("li[data-period=sixMonths]").addClass("active");
            break;
          case "oneYear":
            $("li[data-period=oneYear]").addClass("active");
            break;
          default:
            $("li[data-period=quarter]").addClass("active");
            break;
        }
      },

      renderView: function() {
        $$.renderPeriodRadios();
      },

      enableNotifications: function() {
        return $("div.technical-detail-page").is(":visible");
      }
    }

    var instance = {

      getHistogramsAndPeriod: function (period) {
        var histogramsAndPeriod = {
          histograms: [jsonValue.histograms.twoQuarters, jsonValue.histograms.eighteenBlocksOfFiveDays],
          period: period
        };
        switch (period) {
          case "sixMonths":
            histogramsAndPeriod.histograms = [jsonValue.histograms.twoSixMonths, jsonValue.histograms.sixMonths];
            break;
          case "oneYear":
            histogramsAndPeriod.histograms = [jsonValue.histograms.twoYears, jsonValue.histograms.oneYear]
            break;
          default:
            histogramsAndPeriod.period = "quarter"
        }
        return histogramsAndPeriod;
      },

      extractViewJson: function (termJson, $skillStatisticRequest) {
        skillStatisticRequest = $skillStatisticRequest;
        $$.toViewJson(termJson);
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
      },

      makeTourGuide: function () {
        var tour = new Tour({
            steps: jsonValue.introTour.homePage,
            template: jsonValue.introTour.template
            
        });
        tour.init();
        tour.start();
      }
    }

    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.mouseHover, $$.highLight, $$.enableNotifications);

    return instance;
  });