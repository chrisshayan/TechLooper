angular.module("Header").controller("headerController", [ "$scope", "jsonValue", "headerService", "$location", function($scope, jsonValue, headerService, $location) {

   var chart = headerService.getChart();
   $('.btn-setting').click(headerService.showSetting);
   $("i[techlooper='chartsMenu']").click(headerService.changeChart);
   headerService.reflectChart();
   $scope.langKeys = jsonValue.availableLanguageKeys;
} ]);