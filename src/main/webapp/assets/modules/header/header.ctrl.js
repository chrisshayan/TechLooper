angular.module("Header").controller("headerController", 
   [ "$scope", "jsonValue", "headerService", "$location", function($scope, jsonValue, headerService, $location) {
      
   console.log("Loading header controller");
// $("i[techlooper='chartsMenu']").removeClass('active');
   
   var hashtag = $location.path();
// if (hashtag === jsonValue.charts.pie) {
// $('.fa-pie-chart').addClass('active');
// $('.fa-bubble-chart').removeClass('active');
// }
// else if (hashtag === jsonValue.charts.bubble) {
// $('.fa-bubble-chart').addClass('active');
// $('.fa-pie-chart').removeClass('active');
// }

   $('.btn-setting').click(headerService.showSetting);
// $('.fa-pie-chart').click(headerService.changeChart);
// $('.fa-bubble-chart').click(headerService.changeChart);
   
   
   $("i[techlooper='chartsMenu']").click(headerService.changeChart);
   var chartMenu;
   switch (hashtag) {
   case jsonValue.charts.pie :
      chartMenu  = $("i[techlooper='chartsMenu'].fa-pie-chart")[0];
   case jsonValue.charts.bubble :
      chartMenu = $("i[techlooper='chartsMenu'].fa-bubble-chart")[0];
      headerService.changeChart({
         target : chartMenu
      });
      break;
   default:
   }
   

   $scope.langKeys = jsonValue.availableLanguageKeys;
// headerService.setChart(jsonValue.charts.bubble);
   
} ]);