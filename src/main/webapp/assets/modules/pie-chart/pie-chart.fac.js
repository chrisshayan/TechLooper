angular.module('Pie').factory('pieFactory', function (utils, jsonValue, termService, $route, $translate) {
  var terms = [];
  var data4PieChart = {colors: [], data: [], labels: [], terms: []};
  var innerDonut = utils.isMobile() ? '0%' : '30%';
  var scope;
  //var translate = {};

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

      $.each(terms, function (i, term) {
        if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
          data4PieChart.data.push([term.label, term.count]);
        }
        else {
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
      $translate(["salaryRangeJob", "jobNumber", "salaryRangeInJob", "jobNumberLabel"]).then(function (translate) {
        $$.generateChartData($terms);
        $('.pie-Chart-Container').highcharts({
          colors: data4PieChart.colors,
          chart: {
            backgroundColor: '#f1f3f7',
            plotBorderColor: '#1f1f1f'
          },
          title: {
            text: ''
          },
          legend: {
            itemStyle: {
              color: '#1f1f1f'
            },
            itemHoverStyle: {
              color: '#1f1f1f'
            },
            itemHiddenStyle: {
              color: '#1f1f1f'
            }
          },
          labels: {
            style: {
              color: '#1f1f1f'
            }
          },
          tooltip: {
            formatter: function () {
              var term = terms[this.point.index];
              if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
                return sprintf(translate.salaryRangeJob, term.salRange, term.label);
              }
              return sprintf(translate.jobNumber, term.count, term.label);
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
                  var term = terms[this.point.index];
                  if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
                    return sprintf(translate.jobNumberLabel, term.count, term.label);
                  }
                  else {
                    return sprintf(translate.salaryRangeInJob, term.salRange, term.label);
                  }
                },
                style: {
                  color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
              }
            },
            series: {
              dataLabels: {
                color: '#1f1f1f'
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
          }],
          credits: {//disable Highchart.com text
            enabled: false
          }
        });
        $('tspan[dx=0]').css('font-size', '14px');
      });
    },

    updateViewTerm: function (term) {
      termService.toViewTerm(term);
      if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
        Highcharts.charts[0].series[0]
          .data[data4PieChart.terms.indexOf(term.term)].update([term.label, term.count]);
      }
    },
    switchChartData: function () {
      if (localStorage.getItem("PIE_CHART_ITEM_TYPE") === jsonValue.pieChartType.job) {
        $('.switch-data').find('li').removeClass('active');
        $('.switch-data').find('li[data-chart=JOB]').addClass('active');
      }
      //var key = instance.getChartData();
      $('.switch-data').find('li').on('click', function () {
        $('.switch-data').find('li').removeClass('active');
        if ($(this).attr('data-chart') == 'JOB') {
          localStorage.setItem('PIE_CHART_ITEM_TYPE', jsonValue.pieChartType.job);
          $route.reload();
        }
        else {
          localStorage.setItem('PIE_CHART_ITEM_TYPE', jsonValue.pieChartType.salary);
          $route.reload();
        }
        $(this).addClass('active');
      });
    }
  }

  return instance;
});