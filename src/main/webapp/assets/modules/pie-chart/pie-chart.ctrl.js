angular.module('PieChart').controller('pieController', ["$scope", function($scope) {
    $('.pie-Chart-Container').highcharts({
        colors: ["#2b908f", "#90ee7e", "#f45b5b", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
      "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
	   chart: {
	      backgroundColor: '#2e272a',
	      plotBorderColor: '#606063'
	   },
        title: {
	      text: ''
	   },
        legend: {
	      itemStyle: {
	         color: '#fff'
	      },
	      itemHoverStyle: {
	         color: '#FFF'
	      },
	      itemHiddenStyle: {
	         color: '#fff'
	      }
	   },
	   labels: {
	      style: {
	         color: '#fff'
	      }
	   },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            },
            series: {
		         dataLabels: {
		            color: '#B0B0B3'
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
            size: '100%',
            innerSize: '30%',
            data: [
                ['Java',   10.0],
                ['.Net',       20.8],
                ['Php',    8.5],
                ['Python',     11.5],
                ['Ruby',   10.7],
                ['PM',       4.3],
                ['DBA',    6.0],
                ['BA',     10.2],
                ['QA',   9.8]
            ]
        }]
    });
	$('text[text-anchor=end]').css('display','none');
}]);