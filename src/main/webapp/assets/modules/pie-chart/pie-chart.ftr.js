angular.module('Pie').factory('pieFactory', ["utils", "jsonValue", function(utils, jsonValue) {
    var terms = [];
    var totalJobs = 0;
    var pieJson = [];
    var termsMap = {};
    
    // TODO: use jsonValue
    var colorJson = [ "#2b908f", "#90ee7e", "#f45b5b", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee" ];
    
    var instance =  {
       setTerms : function($terms) {
          terms = $terms;
          totalJobs = utils.sum(terms, "count");
       },
       
       draw : function(item) {
          
       },

       generateChartData : function() {
          pieJson.length = 0;
          $.each(terms, function(index, term) {
             pieJson.push([angular.uppercase(term.name), term.count]);
          });
       },
       
       initializeAnimation : function() {
        instance.generateChartData();
        // var currentTerm = termsMap[terms.termID];
        // if (currentTerm.count === terms.count) {
        // }else{
        //   console.log(1)
        // }
        console.log("pie chart here: ");console.log(pieJson);
        $('.pie-Chart-Container').highcharts({
           chart: {
               plotBackgroundColor: null,
               plotBorderWidth: 1,//null,
               plotShadow: false
           },
           title: {
               text: 'Browser market shares at a specific website, 2014'
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
               }
           },
           series: [{
               type: 'pie',
               name: 'Browser share',
               data: [
                   ['Firefox',   45.0],
                   ['IE',       26.8],
                   {
                       name: 'Chrome',
                       y: 12.8,
                       sliced: true,
                       selected: true
                   },
                   ['Safari',    8.5],
                   ['Opera',     6.2],
                   ['Others',   0.7]
               ]
           }]
       });
//        $('.pie-Chart-Container').highcharts({
//           colors : colorJson,
//           chart : {
//              backgroundColor : '#2e272a',
//              plotBorderColor : '#606063'
//           },
//           title : {
//              text : ''
//           },
//           legend : {
//              itemStyle : {
//                 color : '#fff'
//              },
//              itemHoverStyle : {
//                 color : '#FFF'
//              },
//              itemHiddenStyle : {
//                 color : '#fff'
//              }
//           },
//           labels : {
//              style : {
//                 color : '#fff'
//              }
//           },
//           tooltip : {
//              pointFormat : '{series.name} <b>: {point.percentage:.1f}%</b>'
//           },
//           plotOptions : {
//              pie : {
//                 allowPointSelect : true,
//                 cursor : 'pointer',
//                 dataLabels : {
//                    enabled : true,
//                    //distance: -30,
//                    inside: true,
//                    format : '<b>{point.name}</b>: {point.y}',
//                    style : {
//                       color : (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
//                    }
//                 }
//              },
//              series : {
//                 dataLabels : {
//                    color : '#fff'
//                 },
//                 marker : {
//                    lineColor : '#333'
//                 }
//              },
//              boxplot : {
//                 fillColor : '#505053'
//              },
//              candlestick : {
//                 lineColor : 'white'
//              },
//              errorbar : {
//                 color : 'white'
//              }
//           },
//           series : [ {
//              type : jsonValue.charts.pie,
//              name : 'Jobs',
//              size : '100%',
//              innerSize : '30%',
//              data : pieJson
//           } ]
//        });
        $('tspan[dx=0]').css('font-size', '14px');
        $('text[text-anchor=end]').css('display', 'none');
       }
    }

    return instance;
}]);