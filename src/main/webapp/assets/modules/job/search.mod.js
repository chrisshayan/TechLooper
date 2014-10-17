angular.module("Jobs").directive("searchbox",function() {
   return {
      restrict : "A",
      replace : false,
      templateUrl : "modules/job/searchBox.tpl.html",
      controller : "searchBoxController"
   }
});