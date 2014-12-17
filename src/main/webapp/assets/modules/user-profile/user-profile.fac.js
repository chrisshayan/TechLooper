angular.module('UserProfile').factory('userProfileFactory', function () {
  var instance = {
    customScrollBar: function(){
      $('.bg-item-container').jScrollPane();
    },
    resizeScreen: function(){
      $(window).resize(function () {
        $('.bg-item-container').jScrollPane();
      });
    },
    collapseContent: function(){
      var list = $('.bg-item');
      list.find('h4').on("click", function(){
        if(!$(this).parent().hasClass('active')){
          list.removeClass('active');
          $(this).parent().addClass('active');
          $(this).parent().find('.bg-item-container').jScrollPane();
        }
      });
    },
    naviControl: function(){
      $('.manager-navi').find('.fa-bars').on('tap click', function(){
        if($(this).hasClass('active')){
          $('.main-navi-block').animate({
            width: '0px'
          }).css('position','relative');
          $('.user-profile-page').animate({
            'padding-left': 0
          }, 100, function() {
            $('.bg-item-container').jScrollPane();
          });
          $('.header-section').animate({
            'padding-left': '65px'
          });
          $('.navi-container').hide();
          $(this).removeClass('active');
        }else{
          $('.main-navi-block').animate({
            width: '85px'
          }).css('position','fixed');
          $('.user-profile-page').animate({
            'padding-left': '85px'
          }, 100, function() {
            $('.bg-item-container').jScrollPane();
          });
          $('.header-section').animate({
            'padding-left': '0px'
          });
          $('.navi-container').show();
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
  return instance;
});