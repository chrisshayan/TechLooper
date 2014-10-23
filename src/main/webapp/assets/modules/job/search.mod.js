angular.module("Jobs").directive("searchForm",function() {
   return {
      restrict : "A",
      replace : false,
      templateUrl : "modules/job/search.tpl.html",
      controller : "searchFormController"
   }
});