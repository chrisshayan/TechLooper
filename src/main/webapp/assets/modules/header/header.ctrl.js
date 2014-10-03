angular.module("Header").controller("headerController", ["$scope", "jsonFactory", function($scope, jsonFactory) {

   
   
   
   // TODO: refactor later
   //$scope.keyList = jsonFactory.shortcuts;
   // var key = $(".keyboard");
   // key.on("click", function() {
   //     if ($(".keyboard-shortcuts-items:first").is(":hidden")) {
   //         $(".keyboard-shortcuts-items").slideDown("slow");
   //     } else {
   //         $(".keyboard-shortcuts-items").hide();
   //     }
   // });
   $scope.settingStyle = function() {
      var set = $(".setting-content");
      set.on("click", function() {
         set.find("ul.setting-items").css("display", "block");
         set.stop().animate({
            width : "125" // 125
         });
      }).mouseleave(function() {
         set.find("ul.setting-items").css("display", "none");
         set.stop().animate({
            width : "28px"
         });
         $(".keyboard-shortcuts-items").hide();
      });
   }
   $scope.langKeys = jsonFactory.availableLanguageKeys;

}]);