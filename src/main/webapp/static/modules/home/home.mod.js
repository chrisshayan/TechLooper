angular.module('Home').directive('chart', function() {
   return {
      restrict : 'A', // This mens that it will be used as an attribute and NOT as an element.
      replace : true,
      templateUrl : "modules/collection/chart.tpl.html"
   }
}).directive('find-jobs', function() {
   return {
      restrict : 'A', // This mens that it will be used as an attribute and NOT as an element.
      replace : true,
      templateUrl : "modules/find-jobs/find-jobs.tpl.html"
   }
});
