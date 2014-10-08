
angular.module("Header").controller("headerController", ["$scope", "jsonValue", "headerService", "$location", function($scope, jsonValue, headerService, $location) {
   var path = $location.path();
   if(path == '/pieChart'){
   		$('.fa-pie-chart').addClass('active');
   		$('.fa-bubble-chart').removeClass('active');
   }else{
   		$('.fa-bubble-chart').addClass('active');
   		$('.fa-pie-chart').removeClass('active');
   }
   $('.btn-setting').click(headerService.showSetting);
   $('.fa-pie-chart').click(headerService.changeChart);
   $('.fa-bubble-chart').click(headerService.changeChart);
   $scope.langKeys = jsonValue.availableLanguageKeys;
}]);