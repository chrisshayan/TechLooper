angular.module('Skill').factory('skillChartFactory', function (jsonValue) {
  var instance = {
    renderView: function (skills) {
      var dataChart = instance.getDataForChart(skills);
      var last30Days = instance.getLastDays(skills);
      var max = 0, min = 0;
      for (var i = 0; i < skills.length; i++) {
        nMax = Math.max.apply(null, skills[i].histogramData);
        nMin = Math.min.apply(null, skills[i].histogramData);
        if (nMax > max) {
          max = nMax;
        }
        if (nMin < min) {
          min = nMin;
        }
        if (i == 0) {
          min = nMin;
        }
      }

      var skillColors = [];
      $.each(skills, function(i, skill){skillColors.push(skill.color);});
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
          categories: last30Days,
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
          min: min,
          max: max,
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
                instance.getSkillName(this.name);
              }
            }
          }
        },
        series: dataChart
      });
      $('text[text-anchor=end]').each(function () {
        if ($(this).text() == 'Highcharts.com') {
          $(this).hide();
        }
      });
    },
    getLastDays: function (data) {
      var day = Date.today();
      var arDays = [];
      var NumberDays = data[0].histogramData.length;
      arDays.push(day.toString("MMM d"));
      for (var i = 0; i < NumberDays; i++) {
        day = day.add(-1).days().clone();
        arDays.push(day.toString("MMM d"));
      }
      return arDays.reverse();
    },
    getDataForChart: function (data) {
      var dataItem = [];
      for (var i = 0; i < data.length; i++) {
        dataItem.push({
          name: data[i].skillName,
          data: data[i].histogramData
        });
      }
      return dataItem;
    },
    getSkillName: function (nSkill) {
      var oj = $('.rwd-table').find('tr');
      oj.removeClass('active');
      oj.each(function () {
        if ($(this).find('td[data-th=Skill]').text() == nSkill) {
          $(this).addClass('active');
        }
      });
    }
  }

  return instance;
});