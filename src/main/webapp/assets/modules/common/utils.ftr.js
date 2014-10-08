angular.module("Common").factory("utils", function() {
   return {
      sum : function(array, prop) {
         var total = 0;
         $.each(array, function(index, value) {
            total += value[prop];
         });
         return total;
      }
   }
});