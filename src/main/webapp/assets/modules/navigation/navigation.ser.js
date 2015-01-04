angular.module("Navigation").factory("navigationService", function (utils, jsonValue, $rootScope, $http, $location) {

  var $$ = {
    naviControl: function () {
      $('.manager-navi').find('.fa-bars').on('tap click', function () {
        if ($(this).hasClass('active')) {
          $('.main-navi-block').animate({
            width: '0px'
          }, 300, function () {
            $(this).css('position', 'relative');
          });
          $('.techlooper-body').animate({
            'padding-left': 0
          });
          $('.sub-page-header').animate({
            'padding-left': '90px'
          });
          $('.navi-container').animate({
            'width': '0%'
          }, 300, function () {
            $(this).css('display', 'none');
          });

          $(this).removeClass('active');
        }
        else {
          $('.main-navi-block').animate({
            width: '85px'
          }).css('position', 'fixed');
          $('.techlooper-body').animate({
            'padding-left': '85px'
          });
          $('.sub-page-header').animate({
            'padding-left': '20px'
          });
          $('.navi-container').animate({
            'width': '100%'
          }).css('display', 'block');
          $(this).addClass('active');
        }
      });
    },

    updateChartButton: function () {
      switch (utils.getView()) {
        case jsonValue.views.pieChart:
          $(".m-chart").removeClass('m-pie-chart').addClass('m-bubble-chart');
            //.attr('href', "#" + jsonValue.routerUris.bubble);
          break;

        default:
          $('.m-chart').removeClass('m-bubble-chart').addClass('m-pie-chart')
            //.attr('href', "#" + jsonValue.routerUris.pie);
          break;
      }
    },

    updateSigninButton: function () {
      if ($rootScope.userInfo === undefined) {
        $("a.sign-out-sign-in").attr({'title': 'Sign Out'}).addClass('m-sign-out');
      }
      else {
        $("a.sign-out-sign-in").attr({'title': "Sign In"}).removeClass('m-sign-out');
      }
    },

    registerEventListeners: function () {
      $(".m-chart").on("click tap", function() {
        $location.path($('.m-chart').hasClass("m-pie-chart") ? jsonValue.routerUris.pie : jsonValue.routerUris.bubble);
        $$.updateChartButton();
      });

      $("a.sign-out-sign-in").click(function () {
        if ($rootScope.userInfo === undefined) {
          $location.path(jsonValue.routerUris.signIn);
        }
        else {
          $http.get(jsonValue.httpUri.logout).success(function () {
            utils.sendNotification(jsonValue.notifications.logoutSuccess);
          });
        }
        $$.updateSigninButton();
      });
    }
  }

  var instance = {
    initialize: function () {
      $$.updateChartButton();
      $$.updateSigninButton();
      $$.registerEventListeners();
      $$.naviControl();
    }

    //restartTour: function () {
    //  $('.infor-tour').on('click', function () {
    //    tourService.restart();
    //  });
    //}
  }

  //utils.registerNotification(jsonValue.notifications.userInfo, $$.updateSigninButton);

  return instance;
});