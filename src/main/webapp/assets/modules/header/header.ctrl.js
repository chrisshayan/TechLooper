
angular.module("Header").controller("headerController", ["$scope", "jsonValue", "headerService", function($scope, jsonValue, headerService) {
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
   $scope.langKeys = jsonValue.availableLanguageKeys;

   $('.fa-pie-chart').click(headerService.changeView);
   $('.fa-bubble-chart').click(headerService.changeView);
}]);