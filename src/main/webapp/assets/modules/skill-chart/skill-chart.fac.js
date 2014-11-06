angular.module('Skill').factory('skillChartFactory', function() {
    var last30Days = [];
    
    return instance = {
        draw: function(data) {
            var dataChart = instance.getDataForChart(data);
            instance.getLastDays();
            $('.line-chart-content').highcharts({
            	chart:{
            		backgroundColor : '#201d1e',
                    type: 'spline'
            	},
                colors: ['#f8d303', '#006600', '#dd00d4'],
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
                    tickInterval:1,
                    tickmarkPlacement: 'on',
                    gridLineWidth: 1
                },
                yAxis: {
                    title: {
                        text: 'Percent (%)'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#313131'
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
				    },
                    min: 0, max: 100,
                    tickInterval: 10,
                },
                tooltip: {
                     valueSuffix: 'Jobs'
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
                series: dataChart
            });
        },
        getLastDays: function(){
            var day =  Date.today();
            last30Days.push(day.toString("MMM d"));
            for(var i = 1 ; i < 31; i++){
                day = day.add(-1).days().clone()
                last30Days.push(day.toString("MMM d"));
            }
        },
        getDataForChart: function(data){
            var dataItem = [];
            for(var i = 0; i <3; i++){
                var numbers = instance.getRandomNumber();
                dataItem.push(
                    {   
                        name: data[i].skill, 
                        data: numbers
                    }
                );
            }
            return dataItem;
            
        },
        getRandomNumber: function(){
            var Numbers = [];
            for(var j = 0; j < 30; j++){
                var rdNumber = Math.floor((Math.random() * 100) + 1);
                Numbers.push(rdNumber);
            }
            return Numbers;
        }
    }
});