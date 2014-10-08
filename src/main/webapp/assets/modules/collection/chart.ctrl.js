angular.module('Chart', ["Bubble", "Pie", "Common", "Header"]).controller('chartController', 
   ["$scope", "jsonValue", "connectionFactory", "bubbleFactory", "pieFactory", "utils", "headerService","$rootScope",
    function($scope, jsonValue, connectionFactory, bubbleFactory, pieFactory, utils, headerService, $rootScope) {
        connectionFactory.initialize($scope);
        var events = jsonValue.events;
        var rColor = 0;
        var terms = [];
        var chartFactory = headerService.getChart()
            chart = '';
        var bubbleHTML = '<div id="box" class="bubble-chart-container"></div>',
            pieHTML = '<div class="pie-Chart-Container" style="min-width: 310px; height: 500px; max-width: 800px; margin: 0 auto"></div>';

         $rootScope.$on(jsonValue.events.changeChart, function(event, data) {
            chartFactory = data;
         });
         if (chart == jsonValue.charts.pie) {
            chartFactory = pieFactory;
         }else{
            chartFactory = bubbleFactory;
         }
        if (chartFactory == pieFactory) {
            $('.chart-contrainer').append(pieHTML);
            $('.pie-Chart-Container').highcharts({
                colors: ["#2b908f", "#90ee7e", "#f45b5b", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee",
                    "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"
                ],
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
                        ['Java', 10.0],
                        ['.Net', 20.8],
                        ['Php', 8.5],
                        ['Python', 11.5],
                        ['Ruby', 10.7],
                        ['PM', 4.3],
                        ['DBA', 6.0],
                        ['BA', 10.2],
                        ['QA', 9.8]
                    ]
                }]
            });
            $('text[text-anchor=end]').css('display', 'none');

        } else {
            $('.chart-contrainer').append(bubbleHTML);
            $scope.$on(events.terms, function(event, data) {
                terms = data;
                chart.setTerms(terms);
                $.each(terms, function(index, term) {
                    rColor = rColor + 1;
                    if (rColor == 9) {
                        rColor = 0;
                    }
                    chart.draw({
                        'colorID': rColor,
                        'count': term.count,
                        'termName': term.name,
                        'termID': term.term
                    }, true);

                    $scope.$on(events.term + term.term, function(event, data) {
                        rColor = rColor + 1;
                        if (rColor == 9) {
                            rColor = 0;
                        }
                        chart.draw({
                            'colorID': rColor,
                            'count': data.count,
                            'termName': data.termName,
                            'termID': data.term
                        });
                    });

                });
                chart.initializeAnimation();
            });
            connectionFactory.receiveTechnicalTerms();
        }
    }
]);