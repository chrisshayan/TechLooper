angular.module("Header").factory("headerService", [ 'jsonValue', '$rootScope', "$location", function(jsonValue, $rootScope, $location) {
   var instance = {
      changeChart : function(event) {
         $("i[techlooper='chartsMenu']").removeClass('active');
         $(event.target).addClass("active");
         if (event.notEmit === true) {
            return;
         }
         $rootScope.$emit(jsonValue.events.changeChart, $location.path());
      },
      
      getChart : function() {
         return $location.path();
      },

      getClickedChartStyle : function($target) {
         if ($target.hasClass('fa-pie-chart')) {
            return jsonValue.charts.pie;
         }
         else if ($target.hasClass('fa-bubble-chart')) {
            return jsonValue.charts.bubble;
         }
         return undefined;
      },

      showSetting : function() {
         var set = $(".setting-content");
         if (set.hasClass('hideContent')) {
            set.removeClass('hideContent').addClass('showContent');
            set.find("ul.setting-items").css("display", "block");
            set.stop().animate({
               width : "125"
            });

         }
         else {
            set.removeClass('showContent').addClass('hideContent');
            set.find("ul.setting-items").css("display", "none");
            set.stop().animate({
               width : "28"
            });
         }
      }
   }

   return instance;
} ]);