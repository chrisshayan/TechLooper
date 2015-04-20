techlooper.factory("technicalDetailService", function () {
  var instance = {

    /**
     * @param {object} skill - Skill object @see skill-level-analytics.json
     */
    showSkillsList: function (skill) {
      var currentValueIndex = skill.histograms[0].values.length - 1;
      Circles.create({
        id: 'circles-' + skill.skillName,
        radius: 60,
        value: skill.histograms[0].values[currentValueIndex],
        maxValue: 100,
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
    trendSkills: function () {
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
        series: [{
          name: 'Hestavollane',
          data: [4.3, 5.1, 4.3, 5.2, 5.4, 4.7, 3.5, 4.1, 5.6, 7.4, 6.9, 7.1,
            7.9, 7.9, 7.5, 6.7, 7.7, 7.7, 7.4, 7.0, 7.1, 5.8, 5.9, 7.4,
            8.2, 8.5, 9.4, 8.1, 10.9, 10.4, 10.9, 12.4, 12.1, 9.5, 7.5,
            7.1, 7.5, 8.1, 6.8, 3.4, 2.1, 1.9, 2.8, 2.9, 1.3, 4.4, 4.2,
            3.0, 3.0]

        }, {
          name: 'Voll',
          data: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.3, 0.0,
            0.0, 0.4, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
            0.0, 0.6, 1.2, 1.7, 0.7, 2.9, 4.1, 2.6, 3.7, 3.9, 1.7, 2.3,
            3.0, 3.3, 4.8, 5.0, 4.8, 5.0, 3.2, 2.0, 0.9, 0.4, 0.3, 0.5, 0.4]
        }]
      });
    }
  };
  return instance;

});