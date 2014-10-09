angular.module("Header").factory("headerService",
   [ 'jsonValue', '$rootScope', "$location", "pieFactory", "bubbleFactory", "$cacheFactory", function(jsonValue, $rootScope, $location, pieFactory, bubbleFactory, $cacheFactory) {

      var cache = $cacheFactory("chart");

      var instance = {
         changeChart : function(event) {
//            instance.getChart().$element.addClass("active");
//            if (event !== undefined) {
//               $("i[techlooper='chartsMenu']").removeClass('active');
//               instance.getChart().$element.addClass("active");
//            }
            instance.reflectChart(event);
            $rootScope.$emit(jsonValue.events.changeChart);
         },
         
         reflectChart : function(event) {
            $("i[techlooper='chartsMenu']").removeClass('active');
            var $element = (event !== undefined) ? $(event.target) : instance.getChart($location.path()).$element;
            $element.addClass("active");
         },

         getChart : function(location) {
            var chart = cache.get("chart");
            if (chart !== undefined && chart.location === (location === undefined) ? $location.path() : location) {
               return chart;
            }
            chart = {
               location : $location.path()
            };
            switch (chart.location) {
            case jsonValue.charts.pie:
               chart.$element = $($("i[techlooper='chartsMenu'].fa-pie-chart")[0]);
               chart.factory = pieFactory;
               break;
            case jsonValue.charts.bubble:
               chart.$element = $($("i[techlooper='chartsMenu'].fa-bubble-chart")[0]);
               chart.factory = bubbleFactory;
               break;
            }
            cache.put("chart", chart);
            return chart;
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