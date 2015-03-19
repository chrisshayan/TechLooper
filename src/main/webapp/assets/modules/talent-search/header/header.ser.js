techlooper.factory("tsHeaderService", function () {
  var hWin = $(window).height();  var $$ = {
    closeMenuButton : function() {
      $('.close-menu').find('span').click(function() {
        $$.hideMenu();
      });
    },
    closeMenuLink : function() {
      $('.full-menu').find('a').on('click', function() {
        $$.hideMenu();
      });
    },
    hideMenu: function(){
      $('.close-menu').find('span').animate({
        opacity: 0
      },500);
      $('.full-menu').find('ul').animate({
        opacity: 0
      }, 500);
      $('.full-menu').animate({
        opacity: 0,
        height: 0,
        width: 0,
        left: '50%',
        top: '50%',
        'z-index': '-9'
      },1000);
      $('body').removeClass('noscroll');
      $('.languages-list').removeClass('active');
    },
    managerMenu : function() {
      var ctrlMenu = $('.menu-icon');
      ctrlMenu.click(function() {
        $('.full-menu').css('z-index', '99999');
        $('.full-menu').animate({
          opacity: 1,
          height: hWin,
          width: '100%',
          left: 0,
          top: 0
        },1000,function(){
          $('body').addClass('noscroll');
        });
        $('.close-menu').find('span').animate({
          opacity: 1,
        },1500);
        $('.full-menu').find('ul').animate({
          opacity: 1
        }, 500);

      });
    },
    winResize: function(){
      $( window ).resize(function() {
        hWin = $(window).height();
        $('.full-menu').css('height',hWin);
      });
    },
    scrollToSearchForm: function(){
      $('.search-icon').click(function(){
        if($('.search-form-block').length > 0){
          $('html,body').animate({ scrollTop: $('.search-form-block').offset().top - 75},800);
        }else{
          window.location.href='#/home';
        }
      });
    }
  };
  var instance = {
    init: function(){
      $('body').removeClass('noscroll');
      //$$.closeMenuButton();
      //$$.closeMenuLink();
      //$$.managerMenu();
      $$.winResize();
      $$.scrollToSearchForm();
    },
    menuAnimate: function() {
      var wScroll = $(window).scrollTop();
      if ($('.search-form-block').position().top > -60 <= wScroll) {
        $('header').addClass('changed');
      }
      $(window).scroll(function () {
        wScroll = $(window).scrollTop();
        if (wScroll > 0) {
          var pos = $('.search-form-block').position();
          if (pos !== undefined && pos.top - 60 <= wScroll) {
            $('header').addClass('changed');
          } else {
            $('header').removeClass('changed');
          }
        }
      });
    },
    langManager: function() {
      var ctrlLang = $('.languages'),
          conLang = $('.languages-list');
      ctrlLang.on('click', function() {
        conLang.css('height', 'auto');
        conLang.toggleClass('active');
      });
    },
    settingLang: function(){
      var conLang = $('.languages-list');
      var item = conLang.find('.language-item a');
      item.on('click', function() {
        var lang = $(this).text();
        $('.languages').find('span.text').text(lang);
        conLang.removeClass('active');
      });
    }
  };
  return instance;

});