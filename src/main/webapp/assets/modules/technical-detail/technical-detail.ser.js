techlooper.factory("technicalDetailService", function (utils, $translate, jsonValue, $rootScope) {
  var trendSkillChart = {};
  var fnColor = d3.scale.category10();

  var instance = {

    /**
     * @param {object} skill - Skill object
     * @see skill-level-analytics.json
     */
    showSkillsList: function (skill, maxValue) {
      skill.colors = [];
      skill.colors.unshift(fnColor(skill.id));
      skill.colors.unshift("#e9e8e7");

      Circles.create({
        id: 'circles-' + skill.id,
        radius: 60,
        value: skill.totalJob,
        maxValue: maxValue,
        width: 5,
        text: function (value) {return value;},
        colors: skill.colors,
        duration: 400,
        wrpClass: 'circles-wrp',
        textClass: 'circles-text',
        styleWrapper: true,
        styleText: true
      });
    },

    /**
     * Return Trend skill Line chart configuration
     *
     * @param {object} termStatistic - Term Statistic object
     * @see skill-level-analytics.json
     *
     * @return {object} chartConfig - Trend skill chart configuration
     */
    prepareTrendSkills: function (termStatistic) {
      var chartConfig = {};

      var series = [];
      var yMax = 0;
      var yMin = termStatistic.skills[0].histograms[0].values[0];
      var colors = [];
      var fnColor = d3.scale.category10();
      $.each(termStatistic.skills, function (i, skill) {
        series.push({name: skill.skillName, data: skill.histograms[0].values});
        yMax = Math.max(yMax, skill.histograms[0].values.max());
        yMin = Math.min(yMin, skill.histograms[0].values.min());
        colors.push(fnColor(skill.skillName));
      });

      var labels = [];
      var period = utils.getHistogramPeriod(termStatistic.skills[0].histograms[0].name);
      $.each(termStatistic.skills[0].histograms[0].values, function (i, item) {
        labels.unshift(utils.formatDateByPeriod((i * period).days().ago(), "oneYear"));
      });

      chartConfig = {
        series: series,
        colors: colors,
        yAxis: {min: yMin, max: yMax},
        xAxis: {labels: labels}
      }
      return chartConfig;
    },

    generateTrendSkillsChartOptions: function() {
      var translate = $rootScope.translate;
      return {
        chart: {
          renderTo: 'trendSkills',
          type: 'spline'
        },
        colors: trendSkillChart.config.colors,
        title: {
          text: ''
        },
        subtitle: {
          text: ''
        },
        xAxis: {
          categories: trendSkillChart.config.xAxis.labels,
          gridLineColor: '#353233',
          labels: {
            style: {
              color: '#8a8a8a'
            }
          },
          title: {
            text: translate.timeline
          },
          tickInterval: 1,
          tickmarkPlacement: 'on',
          gridLineWidth: 1
        },
        yAxis: {
          labels: {
            formatter: function () {
              return this.value;
            }
          },
          title: {
            text: translate.numberOfJobs
          },
          min: trendSkillChart.config.yAxis.min,
          max: trendSkillChart.config.yAxis.max,
          tickInterval: 5
        },
        tooltip: {
          valueSuffix: ' ' + translate.jobs
        },
        legend: {
          layout: 'horizontal',
          align: 'center',
          verticalAlign: 'top',
          borderWidth: 0,
          itemStyle: {
            color: '#636363'
          },
          itemHoverStyle: {
            color: '#E0E0E3'
          },
          itemHiddenStyle: {
            color: '#606063'
          }
        },
        plotOptions: {
          series: {
            marker: {
              enabled: false
            },
            lineWidth: 1,
            states: {
              hover: {
                lineWidth: 3
              }
            }
          }
        },
        series: trendSkillChart.config.series,
        credits: {//disable Highchart.com text
          enabled: false
        }
      }
    },

    createTrendSkillsChart: function() {
      return new Highcharts.Chart(instance.generateTrendSkillsChartOptions());
    },

    /**
     * @param {object} termStatistic - Term Statistic object @see skill-level-analytics.json
     */
    trendSkills: function (termStatistic) {
      trendSkillChart.config = instance.prepareTrendSkills(termStatistic);
      trendSkillChart.instance && trendSkillChart.instance.destroy();
      trendSkillChart.instance = instance.createTrendSkillsChart();
    },

    enableNotifications: function () {
      return utils.getView() === jsonValue.views.analyticsSkill;
    }
  };

  return instance;
});