angular.module("Chart").directive("chartview", ["$location", "jsonValue", function($location, jsonValue) {
   var template;
   switch ($location.path()) {
   case jsonValue.charts.pie:
      template = "modules/pie-chart/pie-chart.tpl.html";
      break;
   case jsonValue.charts.bubble:
      template = "modules/bubble-chart/bubble-chart.tpl.html";
      break;
   default:
   }
   
   return {
      restrict : "A", // This mens that it will be used as an attribute and NOT as an element.
      replace : false,
      templateUrl : template
   }
}]);
