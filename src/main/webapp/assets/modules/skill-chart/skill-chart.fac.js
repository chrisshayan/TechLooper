angular.module('Skill').factory('skillChartFactory', function() {
    return {
        draw: function(data) {
            $('.line-chart-content').highcharts({
            	chart:{
            		backgroundColor : '#201d1e'
            	},
                title: {
                    text: '',
                    x: -20, //center,
                    style: {
				         color: '#E0E0E3',
				         textTransform: 'uppercase',
				         fontSize: '20px'
				      }
                },
                subtitle: {
                    text: '',
                    x: -20
                },
                xAxis: {
                    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                        'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
                    ],
                    gridLineColor: '#353233',
			        labels: {
			        	style: {
			            color: '#8a8a8a'
			        	}
			        }
                },
                yAxis: {
                    title: {
                        text: '%'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#353233'
                    }],
			        title: {
			        	style: {
			            color: '#8a8a8a'
			        	}
			        },
			        labels: {
				        style: {
				        	color: '#8a8a8a'
				        }
				    }
                },
                tooltip: {
                    // valueSuffix: '°C'
                },
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'top',
                    borderWidth: 0,
                    itemStyle: {
				         color: '#E0E0E3'
				      },
				      itemHoverStyle: {
				         color: '#8a8a8a'
				      },
				      itemHiddenStyle: {
				         color: '#606063'
				      }
                },
                series: [{
                    name: data[0].skill,
                    data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
                }, {
                    name: data[1].skill,
                    data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
                }, {
                    name: data[2].skill,
                    data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
                }]
            });
        }
    }
});