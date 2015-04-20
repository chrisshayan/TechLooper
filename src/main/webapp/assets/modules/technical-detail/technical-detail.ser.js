techlooper.factory("technicalDetailService", function () {
  var instance = {

    /**
     * @param {object} skill - Skill object @see skill-level-analytics.json
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
     * @param {object} termStatistic - Term Statistic object @see skill-level-analytics.json
     */
    prepareTrendSkills: function(termStatistic) {
      var data = [];
      $.each(termStatistic.skills, function(i, skill) {
        data.push({name: skill.skillName, data: skill.histograms[0].values});
      });
      return data;
    },

    /**
     * @param {object} termStatistic - Term Statistic object @see skill-level-analytics.json
     */
    trendSkills: function (termStatistic) {
      var series = instance.prepareTrendSkills(termStatistic);
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
          //categories: chartMetadata.xAxisLabels,
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
          //min: chartMetadata.minMax.min,
          //max: chartMetadata.minMax.max,
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
        series: series
      });
    }
  };
  return instance;

});