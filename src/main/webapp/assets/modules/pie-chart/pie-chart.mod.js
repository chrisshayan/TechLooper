angular.module("PieChart").directive("chart", function() {
   return {
      restrict : "A", // This mens that it will be used as an attribute and NOT as an element.
      replace : true,
      templateUrl : "modules/collection/chart.tpl.html",
      controller : "chartController"
   }
}).directive("findjobs", function() {
   return {
      restrict : "A", // This mens that it will be used as an attribute and NOT as an element.
      replace : true,
      templateUrl : "modules/job/findJobs.button.tpl.html",
      controller: 'findJobsController'
   }
});
