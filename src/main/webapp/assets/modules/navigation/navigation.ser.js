angular.module("Navigation").factory("navigationService",
  function (utils, jsonValue, $rootScope, $location, pieFactory,
            bubbleFactory, $cacheFactory, tourService, localStorageService, $http) {

    var $$ = {
      updateUserInfo: function() {
        if (localStorageService.get(jsonValue.storage.key)) {//already sign-in
          $("a.sign-out-sign-in").attr({"href": "#", 'title':'Sign Out'}).addClass('m-sign-out');

          $("a.sign-out-sign-in").click(function() {
            $http.get(jsonValue.httpUri.logout).success(function() {
              localStorageService.clearAll();
              //delete $cookies['JSESSIONID'];
              $location.path("/");
            });
          });
        }
        else {//already sign-out
          $("a.sign-out-sign-in").unbind("click");
          $("a.sign-out-sign-in").attr({"href": "#/signin", 'title': "Sign In"}).removeClass('m-sign-out');
        }
      },
      naviControl: function(){
        $('.manager-navi').find('.fa-bars').on('tap click', function(){
          if($(this).hasClass('active')){
            $('.main-navi-block').animate({
              width: '0px'
            }, 300, function(){
              $(this).css('position','relative');
            });
            $('.techlooper-body').animate({
              'padding-left': 0
            });
            $('.sub-page-header').animate({
              'padding-left': '80px'
            });
            $('.navi-container').animate({
              'width': '0%'
            }, 300, function(){
              $(this).css('display', 'none');
            });

            $(this).removeClass('active');
          }else{
            $('.main-navi-block').animate({
              width: '85px'
            }).css('position','fixed');
            $('.techlooper-body').animate({
              'padding-left': '85px'
            });
            $('.sub-page-header').animate({
              'padding-left': '0px'
            });
            $('.navi-container').animate({
              'width': '100%'
            }).css('display', 'block');
            $(this).addClass('active');
          }
        });
      },
      subNaviControl: function(){
        var subNavi = $('.hi-icon-effect-1').find('li.subNavi');
        subNavi.mouseenter(function(){
          $(this).find('ul').stop().animate({
            width: '140px',
            'padding-left': '40px'
          }).css('z-index','666');
        }).mouseleave(function(){
          $(this).find('ul').stop().animate({
            width: '0',
            'padding-left': '0',
            'z-index': '-666'
          });
        });
      }
    }

    var instance = {
      initialize: function() {
        $$.updateUserInfo();
        $$.naviControl();
        $$.subNaviControl();
      }
    }

    return instance;
  });
