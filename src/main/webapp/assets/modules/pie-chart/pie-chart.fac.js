angular.module('Pie').factory('pieFactory', function (utils, jsonValue, termService) {
  var terms = [];
  var data4PieChart = {colors: [], data: [], labels: [], terms: []};
  var innerDonut = utils.isMobile() ? '0%' : '30%';
  var scope;
  //var labels = [];

  var $$ = {
    enableNotifications: function () {
      return utils.getView() === jsonValue.views.pieChart;
    },

    generateChartData: function ($terms) {
      terms = termService.toViewTerms($terms);
      var total = $$.getTotalJob(terms);
      $.each(terms, function (i, term) {
        var per = term.averageSalaryMin / total * 100;
        if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
          per = term.count / total * 100;
        }
        term.percent = per.toFixed(1);
      });
      scope.terms = terms;
      scope.$apply();

      $.each(terms, function (i, term) {
        if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
          data4PieChart.data.push([term.label, term.count]);
        } else {
          data4PieChart.data.push([term.label, term.averageSalaryMin, term.salRange]);
        }
        data4PieChart.colors.push(term.color);
      });
      data4PieChart.terms = terms.toArray("term");
      data4PieChart.labels = terms.toArray("label");
    },

    switchScope: function ($scope) {
      scope = $scope;
      data4PieChart = {colors: [], data: [], labels: [], terms: []};
    },
    getTotalJob: function (terms) {
      var total = 0;
      $.each(terms, function (index, item) {
        total = total + item.count;
      })
      return total;
    }
  }
  utils.registerNotification(jsonValue.notifications.switchScope, $$.switchScope, $$.enableNotifications);

  var instance = {
    renderView: function ($terms) {
      $$.generateChartData($terms);
      $('.pie-Chart-Container').highcharts({
        colors: data4PieChart.colors,
        chart: {
          backgroundColor: '#201d1e',
          plotBorderColor: '#efefef'
        },
        title: {
          text: ''
        },
        legend: {
          itemStyle: {
            color: '#efefef'
          },
          itemHoverStyle: {
            color: '#efefef'
          },
          itemHiddenStyle: {
            color: '#efefef'
          }
        },
        labels: {
          style: {
            color: '#efefef'
          }
        },
        tooltip: {
          formatter: function () {
            if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
              return sprintf("<b>%(salRange)s</b> <br/>a month in average for jobs in <b>%(label)s</b>", terms[this.point.index]);
            }
            return sprintf("<b>%(count)s</b> jobs in <b>%(label)s</b>", terms[this.point.index]);
          }
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              //format: '<b>{point.y}</b> jobs in <b>{point.name}</b>',
              formatter: function () {
                var termLabel = this.key;
                var index = data4PieChart.labels.indexOf(termLabel);
                if (index !== -1) {
                  if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
                    return "<span>" + data4PieChart.data[index][1] + ' jobs in ' + termLabel + "</span>";
                  } else {
                    return "<span>" + data4PieChart.data[index][2] + ' in ' + termLabel + "</span>";
                  }
                }
              },
              style: {
                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
              }
            }
          },
          series: {
            dataLabels: {
              color: '#efefef'
            },
            marker: {
              lineColor: '#333'
            },
            animation: false
          },
          boxplot: {
            fillColor: '#505053'
          },
          candlestick: {
            lineColor: 'white'
          },
          errorbar: {
            color: 'white'
          }
        },
        series: [{
          type: 'pie',
          name: 'Jobs',
          innerSize: innerDonut,
          point: {
            events: {
              click: function (e) {
                utils.go2SkillAnalyticPage(scope, terms[this.index].term);
              }
            }
          },
          data: data4PieChart.data
        }]
      });
      $('tspan[dx=0]').css('font-size', '14px');
      $('text[text-anchor=end]').css('display', 'none');
    },

    updateViewTerm: function (term) {
      termService.refineTerm(term);
      if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
        Highcharts.charts[0].series[0]
          .data[data4PieChart.terms.indexOf(term.term)].update([term.label, term.count]);
      }
    },
    switchChartData: function(){
      //var key = instance.getChartData();
      $('.switch-data').find('li').on('click', function(){
        $('.switch-data').find('li').removeClass('active');
        if($(this).attr('data-chart') == 'JOB'){
          localStorage.setItem('PIE_CHART_ITEM_TYPE',jsonValue.pieChartType.job);
        }else{
          localStorage.setItem('PIE_CHART_ITEM_TYPE',jsonValue.pieChartType.salary);
        }
        $(this).addClass('active');
      });
    }
  }

  return instance;
});