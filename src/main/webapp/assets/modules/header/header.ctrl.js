angular.module("Header").controller("headerController",
   [ "$scope", "jsonValue", "headerService", "$location",  function($scope, jsonValue, headerService, $location) {

   var hashtag = $location.path();
   $('.btn-setting').click(headerService.showSetting);
   $("i[techlooper='chartsMenu']").click(headerService.changeChart);
   var chartMenu;
   switch (hashtag) {
   case jsonValue.charts.pie:
      chartMenu = $("i[techlooper='chartsMenu'].fa-pie-chart")[0];
      break;
   case jsonValue.charts.bubble:
      chartMenu = $("i[techlooper='chartsMenu'].fa-bubble-chart")[0];
      break;
   default:
   }
   headerService.changeChart({
      target : chartMenu,
      notEmit : true
   });

   $scope.langKeys = jsonValue.availableLanguageKeys;
} ]);