angular.module('Skill').factory('skillChartFactory', function() {
    return instance = {
        draw: function(data) {
            var dataChart = instance.getDataForChart(data);
            var last30Days = instance.getLastDays(data);
            var max = 0, min = 0;
            for (var i = 0; i < 3; i++) {
                nMax =  Math.max.apply(null, data[i].histogramData);
                nMin =  Math.min.apply(null, data[i].histogramData);
                if(nMax > max){
                    max = nMax;
                }
                if(nMin < min){
                    min = nMin;
                }
                if(i == 0){
                    min = nMin;
                }
            }

            $('.line-chart-content').highcharts({
                chart: {
                    backgroundColor: '#201d1e',
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
                        formatter: function() {
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
                        }
                    }
                },
                series: dataChart
            });
            $('text[text-anchor=end]').each(function(){
                if($(this).text() == 'Highcharts.com'){
                    $(this).hide();
                }
            });

            console.log(min + ' : '+ max)
            
        },
        getLastDays: function(data) {
            var day = Date.today();
            var arDays = [];
            var NumberDays = data[0].histogramData.length;
            arDays.push(day.toString("MMM d"));
            for (var i = 0; i < NumberDays; i++) {
                day = day.add(-1).days().clone()
                arDays.push(day.toString("MMM d"));
            }
            return arDays.reverse();
        },
        getDataForChart: function(data) {
            var dataItem = [];
            for (var i = 0; i < 3; i++) {
                dataItem.push({
                    name: data[i].skillName,
                    data: data[i].histogramData
                });
            }
            return dataItem;

        }
    }
});