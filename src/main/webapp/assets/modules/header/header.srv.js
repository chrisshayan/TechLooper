angular.module("Header").factory("headerService", [ 'jsonValue', '$rootScope', "$location", function(jsonValue, $rootScope, $location) {
   var instance = {
      changeChart : function(event) {
//         console.log($location.path());
         $("i[techlooper='chartsMenu']").removeClass('active');
         $(event.target).addClass("active");
         
//         var chart = instance.getClickedChartStyle($(event.target));
//         if (chart === chartStyle) {
//            return;
//         }
//
//         var item = $(event.target).parent().parent().find('i');
//         item.removeClass('active');
//         $(event.target).addClass('active');
//         chartStyle = chart;
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