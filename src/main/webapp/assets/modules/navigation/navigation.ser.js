angular.module("Navigation").factory("navigationService", function (utils, jsonValue, $rootScope, $http, $location, tourService, userService) {

  var $$ = {
    naviControl: function () {
      $('.manager-navi').find('.fa-bars').on('tap click', function () {
        if ($(this).hasClass('active')) {
          $('.main-navi-block').animate({
            width: '0px'
          }, 300, function () {
            $(this).css('position', 'relative');
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
          var view = utils.getView();
          if(view == jsonValue.views.jobsSearch){
            $('.main-navi-block').css('background','url(images/line-h1.png) #ccc right top repeat-y');
          }
          $('.main-navi-block').animate({
            width: '85px'
          }).css('position', 'fixed');
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
          $('.m-chart').removeClass('m-bubble-chart').addClass('m-pie-chart');
            //.attr('href', "#" + jsonValue.routerUris.pie);
          break;
      }
    },

    registerEventListeners: function () {
      $(".m-chart").on("click tap", function() {
        $location.path($('.m-chart').hasClass("m-pie-chart") ? jsonValue.routerUris.pie : jsonValue.routerUris.bubble);
        $$.updateChartButton();
      });

      $("a.sign-out-sign-in").on("click tap", function () {
        if ($rootScope.userInfo === undefined) {
          $location.path(jsonValue.routerUris.signIn);
        }
        else {
          $http.get(jsonValue.httpUri.logout).success(function () {
            utils.sendNotification(jsonValue.notifications.logoutSuccess);
          });
        }
      });
    },
    updateHighlight: function(){
      $('.navi-container').find('li').removeClass('active');
      switch (utils.getView()) {
        case jsonValue.views.pieChart:
          $('.navi-container').find('a.m-chart').parent().addClass('active');
          break;
        case jsonValue.views.bubbleChart:
          $('.navi-container').find('a.m-chart').parent().addClass('active');
          break;
        case jsonValue.views.jobsSearch:
          $('.navi-container').find('a.m-search-jobs').parent().addClass('active');
          $('.main-navi-block').css('background','url(images/line-h1.png) #ccc right top repeat-y');
          break;
        case jsonValue.views.signIn:
          $('.navi-container').find('a.sign-out-sign-in').parent().addClass('active');
          break;
        default:
          $('.navi-container').find('a.m-chart').parent().addClass('active');
          break;
      }
      var menuItem = $('.navi-container').find('li');
      menuItem.on('click', function(){
        //utils.hideNavigationBar();
        if($(this).find('a').hasClass('m-chart') || $(this).find('a').hasClass('m-search-jobs') || $(this).find('a').hasClass('sign-out-sign-in')){
          menuItem.removeClass('active');
          $(this).addClass('active');
          if($(this).find('a').hasClass('m-search-jobs')){
            $('.main-navi-block').css('background','url(images/line-h1.png) #ccc right top repeat-y');
          }else{
            $('.main-navi-block').css('background','url(images/line-h.png) #000 right top repeat-y');
            $('body').css('background-color','#201d1e');
          }
          if($(this).find('a').hasClass('m-sign-out')){
            $('.navi-container').find('a.sign-out-sign-in').parent().removeClass('active');
            $('.navi-container').find('a.m-chart').parent().addClass('active');
          }
        }
      });
    }
  }
  var instance = {
    initialize: function () {
      $$.updateChartButton();
      $$.updateHighlight();
      $$.registerEventListeners();
      $$.naviControl();
    },

    restartTour: function () {
      $('.infor-tour').on('click', function () {
        tourService.restart();
      });
    }
  }

  return instance;
});