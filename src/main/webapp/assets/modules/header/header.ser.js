angular.module("Header").factory("headerService",
  function (utils, jsonValue, $rootScope, $location, pieFactory,
            bubbleFactory, $cacheFactory, tourService, localStorageService, $http) {
    var cache = $cacheFactory("chart");

    var $$ = {
      updateUserInfo: function() {
        if (localStorageService.get(jsonValue.storage.key)) {//already sign-in
          $(".signin-signout-container > a").attr("href", "#").find('span').addClass('signout');
          $(".signin-signout-container > a").click(function() {
            $http.get(jsonValue.httpUri.logout).success(function() {
              localStorageService.clearAll();
              //delete $cookies['JSESSIONID'];
              $location.path("/");
            });
          });
        }
        else {//already sign-out
          $(".signin-signout-container > a").unbind("click");
          $(".signin-signout-container > a").attr("href", "#/signin").find('span').removeClass('signout');
        }
      }
    }

    var instance = {
      initialize: function() {
        $$.updateUserInfo();
        instance.getChart();
        $('.btn-setting').click(instance.showSetting);
        $("i[techlooper='chartsMenu']").click(instance.changeChart);
        instance.changeChart();
        instance.restartTour();
      },

      changeChart: function (event) {
        $("i[techlooper='chartsMenu']").removeClass('active');
        var $element = (event !== undefined) ? $(event.target) : instance.getChart($location.path()).$element;
        $element !== undefined && $element.addClass("active");
      },

      getChart: function (location) {
        var chart = cache.get("chart");
        if (chart !== undefined && chart.location === (location === undefined) ? $location.path() : location) {
          return chart;
        }
        chart = {
          location: $location.path()
        };
        switch (utils.getView()) {
          case jsonValue.views.pieChart:
            chart.$element = $($("i[techlooper='chartsMenu'].fa-pie-chart")[0]);
            chart.factory = pieFactory;
            break;
          case jsonValue.views.bubbleChart:
            chart.$element = $($("i[techlooper='chartsMenu'].fa-bubble-chart")[0]);
            chart.factory = bubbleFactory;
            break;
        }
        cache.put("chart", chart);
        return chart;
      },

      showSetting: function () {
        var set = $(".setting-content");
        if (!utils.isMobile()) {
          if (set.hasClass('hideContent')) {
            set.removeClass('hideContent').addClass('showContent');
            set.find("ul.setting-items").css("display", "block");
            set.stop().animate({
              width: "125"
            });

          }
          else {
            set.removeClass('showContent').addClass('hideContent');
            set.find("ul.setting-items").css("display", "none");
            set.stop().animate({
              width: "28"
            });
          }
        }
        else {
          if (set.hasClass('hideContent')) {
            set.removeClass('hideContent').addClass('showContent');
            set.find("ul.setting-items").css({
              "display": "block",
              "padding-top": "10px",
              "float": "left"
            });
            set.find("ul.setting-items").find('li').css('padding-left', '0px');
            set.stop().animate({
              'height': "110",
              'width': "28",
              'max-height': "inherit"
            });

          }
          else {
            set.removeClass('showContent').addClass('hideContent');
            set.find("ul.setting-items").css("display", "none");
            set.stop().animate({
              'height': "30",
              'width': "28"
            });
          }

        }
      },
      restartTour: function(){
        $('.infor-tour').on('click', function(){
          tourService.restart();
        });
      }
    }

    return instance;
  });
