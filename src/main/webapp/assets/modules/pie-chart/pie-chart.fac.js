angular.module('Pie').factory('pieFactory', function (utils, jsonValue, termService) {
  var terms = [];
  var data4PieChart = {colors: [], data: []};
  var innerDonut = utils.isMobile() ? '0%' : '30%';
  var scope;
  var labels = [];

  var $$ = {
    able2SwitchScope: function () {
      return $(".pie-Chart-Container").is(":visible");
    },

    generateChartData: function ($terms) {
      terms = termService.toViewTerms($terms);
      $.each(terms, function (i, term) {
        data4PieChart.data.push([term.label, term.count]);
        data4PieChart.colors.push(term.color);
      });
      labels = terms.toArray("label");
    },

    switchScope: function($scope) {
      scope = $scope;
      data4PieChart = {colors: [], data: []};
    }
  }
  utils.registerNotification(jsonValue.notifications.switchScope, $$.switchScope, $$.able2SwitchScope);
  //utils.registerNotification(jsonValue.notifications.changeLang, $$.changeLang, $$.able2SwitchScope);

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
          pointFormat: '{series.name} <b>: {point.percentage:.1f}%</b>'
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.y}',
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
            }
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
                utils.go2SkillAnalyticPage(scope, terms[labels.indexOf(this.name)].term);
              }
            }
          },
          data: data4PieChart.data
        }]
      });
      $('tspan[dx=0]').css('font-size', '14px');
      $('text[text-anchor=end]').css('display', 'none');
    },

    updateViewTerm: function(term) {
      //if (newJson.length > 8) {
      //  newJson.length = 0;
      //}
      //
      //if (force !== true && termsMap[pieItem.termID] !== undefined) {
      //  newJson.push([pieItem.termName, pieItem.count]);
      //  var chart = $('.pie-Chart-Container').highcharts();
      //  $.each(newJson, function (index, item) {
      //    chart.series[0].data[index].update(item);
      //  });
      //  return;
      //}
      //termsMap[pieItem.termID] = pieItem;
      //$('.loading-data').hide();
    }
  }

  return instance;
});