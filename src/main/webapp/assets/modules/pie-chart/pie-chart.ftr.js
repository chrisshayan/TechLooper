angular.module('Pie').factory('pieFactory', ["utils", "jsonValue", function(utils, jsonValue) {
    var terms = [];
    var totalJobs = 0;
    var pieJson = [];
    
    var instance =  {
       setTerms : function($terms) {
          terms = $terms;
          totalJobs = utils.sum(terms, "count");
       },
       
       draw : function(item) {
          
       },

       generateChartData : function() {
          $.each(terms, function(index, term) {
              pieJson.push({
               term : term.name,
               percent : Math.round(parseInt(term.count) * 100 / totalJobs)
             });
          });
         console.log(pieJson)
       },
       
       initializeAnimation : function() {
        console.log("pie initializeAnimation");

        instance.generateChartData();
        $('.pie-Chart-Container').highcharts({
           colors : [ "#2b908f", "#90ee7e", "#f45b5b", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee" ],
           chart : {
              backgroundColor : '#2e272a',
              plotBorderColor : '#606063'
           },
           title : {
              text : ''
           },
           legend : {
              itemStyle : {
                 color : '#fff'
              },
              itemHoverStyle : {
                 color : '#FFF'
              },
              itemHiddenStyle : {
                 color : '#fff'
              }
           },
           labels : {
              style : {
                 color : '#fff'
              }
           },
           tooltip : {
              pointFormat : '{series.name}: <b>{point.percentage:.1f}%</b>'
           },
           plotOptions : {
              pie : {
                 allowPointSelect : true,
                 cursor : 'pointer',
                 dataLabels : {
                    enabled : true,
                    format : '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style : {
                       color : (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                 }
              },
              series : {
                 dataLabels : {
                    color : '#B0B0B3'
                 },
                 marker : {
                    lineColor : '#333'
                 }
              },
              boxplot : {
                 fillColor : '#505053'
              },
              candlestick : {
                 lineColor : 'white'
              },
              errorbar : {
                 color : 'white'
              }
           },
           series : [ {
              type : jsonValue.charts.pie,
              name : 'Jobs',
              size : '100%',
              innerSize : '30%',
              data : pieJson
           } ]
        });
        $('text[text-anchor=end]').css('display', 'none');
       }
    }

    return instance;
}]);