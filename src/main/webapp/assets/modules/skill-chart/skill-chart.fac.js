angular.module('Skill').factory('skillChartFactory', function (jsonValue, utils) {
  var $$ = {
    getXAxisLabels: function (viewJson) {
      var oneSkill = viewJson.tableAndChartJson[0];
      var labels = [];
      $.each(oneSkill.histogramData, function(i, item) {
        labels.unshift((i * oneSkill.histogramDataPeriod).days().ago().toString("MMM d"));
      });
      return labels;
    },

    getSeries: function (viewJson) {
      var series = [];
      $.each(viewJson.tableAndChartJson, function (i, skill) {
        series.push({name: skill.skillName, data: skill.histogramData});
      });
      return series;
    },

    getMinMax: function (viewJson) {
      var skills = viewJson.tableAndChartJson;
      var max = 0;
      var min = skills[0].histogramData[0];
      $.each(skills, function (i, skill) {
        max = Math.max(max, skill.histogramData.max());
        min = Math.min(min, skill.histogramData.min());
      });
      return {min: min, max: max};
    },

    getSkillColors: function (viewJson) {
      var skillColors = [];
      $.each(viewJson.tableAndChartJson, function (i, skill) {skillColors.push(skill.color);});
      return skillColors;
    }
  }

  var instance = {
    renderView: function (viewJson) {
      //var skills = viewJson.tableAndChartJson;
      var series = $$.getSeries(viewJson);// render line
      var xAxisLabels = $$.getXAxisLabels(viewJson);//render lables in Ox
      var minMax = $$.getMinMax(viewJson);
      var skillColors = $$.getSkillColors(viewJson);

      $('.line-chart-content').highcharts({
        chart: {
          backgroundColor: '#201d1e',
          type: 'spline'
        },
        colors: skillColors,
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
          categories: xAxisLabels,
          gridLineColor: '#353233',
          labels: {
            style: {
              color: '#8a8a8a'
            }
          },
          tickInterval: 1,
          tickmarkPlacement: 'on',
          gridLineWidth: 1,
          title: {
            text: 'Last 30 Days'
          }
        },
        yAxis: {
          plotLines: [{
            value: 0,
            width: 1,
            color: '#313131'
          }],
          title: {
            style: {
              color: '#8a8a8a'
            },
            text: 'Numbers of Job'
          },
          labels: {
            formatter: function () {
              return this.value;
            },
            style: {
              color: '#8a8a8a'
            }
          },
          min: minMax.min,
          max: minMax.max,
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
            },
            events: {
              mouseOver: function () {
                utils.sendNotification(jsonValue.notifications.mouseHover, this.name);
              }
            }
          }
        },
        series: series
      });
      $('text[text-anchor=end]').each(function () {
        if ($(this).text() == 'Highcharts.com') {
          $(this).hide();
        }
      });
    },

    highLight: function (skillName) {
      var chart = Highcharts.charts[0];
      $.each(chart.series, function (i, line) {
        line.setState(line.name === skillName ? "hover" : "");
      });
    }
  }

  return instance;
});