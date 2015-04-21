techlooper.factory("technicalDetailService", function (utils) {
  var instance = {

    /**
     * @param {object} skill - Skill object
     * @see skill-level-analytics.json
     */
    showSkillsList: function (skill, maxValue) {
      Circles.create({
        id: 'circles-' + skill.skillName,
        radius: 60,
        value: skill.totalJob,
        maxValue: maxValue,
        width: 10,
        text: function (value) {return value;},
        colors: ['#D3B6C6', '#4B253A'],
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
      $.each(termStatistic.skills, function (i, skill) {
        series.push({name: skill.skillName, data: skill.histograms[0].values});
        yMax = Math.max(yMax, skill.histograms[0].values.max());
        yMin = Math.min(yMin, skill.histograms[0].values.min());
      });
      chartConfig.series = series;
      chartConfig.yAxis = {min: yMin, max: yMax};

      var labels = [];
      var period = utils.getHistogramPeriod(termStatistic.skills[0].histograms[0].name);
      $.each(termStatistic.skills[0].histograms[0].values, function (i, item) {
        labels.unshift(utils.formatDateByPeriod((i * period).days().ago(), "oneYear"));
      });
      chartConfig.xAxis = {labels: labels};

      return chartConfig;
    },

    /**
     * @param {object} termStatistic - Term Statistic object @see skill-level-analytics.json
     */
    trendSkills: function (termStatistic) {
      var chartConfig = instance.prepareTrendSkills(termStatistic);
      $('.trend-chart').highcharts({
        chart: {
          //backgroundColor: '#201d1e',
          type: 'spline'
        },
        //colors: chartMetadata.skillColors,
        title: {
          text: '',
          style: {
            color: '#E0E0E3',
            textTransform: 'uppercase',
            fontSize: '20px'
          }
        },
        subtitle: {
          text: ''
        },
        xAxis: {
          categories: chartConfig.xAxis.labels,
          gridLineColor: '#353233',
          labels: {
            style: {
              color: '#8a8a8a'
            }
          },
          tickInterval: 1,
          tickmarkPlacement: 'on',
          gridLineWidth: 1
        },
        yAxis: {
          plotLines: [{
            value: 0,
            width: 1,
            color: '#313131'
          }],
          labels: {
            formatter: function () {
              return this.value;
            },
            style: {
              color: '#8a8a8a'
            }
          },
          min: chartConfig.yAxis.min,
          max: chartConfig.yAxis.max,
          tickInterval: 10,
          gridLineWidth: 1,
          gridLineColor: '#353233'
        },
        tooltip: {
          valueSuffix: ' Jobs'
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
        series: chartConfig.series
      });
    }
  };
  return instance;

});