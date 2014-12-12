angular.module('Skill').factory('userProfileFactory', function (jsonValue, utils, $translate) {
  var $$ = {
    getXAxisLabels: function (viewJson) {
      var oneSkill = viewJson.tableAndChartJson[0];
      var labels = [];
      $.each(oneSkill.histogramData, function (i, item) {
        labels.unshift((i * viewJson.histogramDataPeriod).days().ago().toString("MMM d"));
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
    },

    getChartMetadata: function (viewJson) {
      return {
        series: $$.getSeries(viewJson),
        xAxisLabels: $$.getXAxisLabels(viewJson),
        minMax: $$.getMinMax(viewJson),
        skillColors: $$.getSkillColors(viewJson),
        xAxisTitle: {},
        yAxisTitle: {
          style: {color: '#8a8a8a'}
        }
      }
    },

    translate: function(chartMetadata) {
      var chart = Highcharts.charts[0];
      $translate("skillLineChartXAxis").then(function(translation){
        chartMetadata.xAxisTitle.text = translation;
        chart.xAxis[0].setTitle(chartMetadata.xAxisTitle);
      });
      $translate("skillLineChartYAxis").then(function(translation){
        chartMetadata.yAxisTitle.text = translation;
        chart.yAxis[0].setTitle(chartMetadata.yAxisTitle);
      });
    }
  }

  var instance = {
    renderView: function (viewJson) {
      var chartMetadata = $$.getChartMetadata(viewJson);
      $('.line-chart-content').highcharts({
        chart: {
          backgroundColor: '#201d1e',
          type: 'spline'
        },
        colors: chartMetadata.skillColors,
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
          categories: chartMetadata.xAxisLabels,
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
          min: chartMetadata.minMax.min,
          max: chartMetadata.minMax.max,
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
        series: chartMetadata.series
      });
      $('text[text-anchor=end]').each(function () {
        if ($(this).text() === 'Highcharts.com') {
          $(this).hide();
        }
      });
      $$.translate(chartMetadata);
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