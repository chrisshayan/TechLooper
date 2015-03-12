techlooper.factory("tsHeaderService", function () {
  var hWin = $(window).height();
  var ctrlClose = $('.close-menu');
  var ctrlLang = $('.languages'),
      conLang = $('.languages-list');
  var $$ = {
    managerClose : function() {
      ctrlClose.find('span').click(function() {
        $(this).animate({
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
      });
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
        },1000);
        ctrlClose.find('span').animate({
          opacity: 1,
        },1500);
        $('.full-menu').find('ul').animate({
          opacity: 1
        }, 500);
        $('body').addClass('noscroll');
      });
    },
    winResize: function(){
      $( window ).resize(function() {
        hWin = $(window).height();
      });
    },
    langManager: function() {
      ctrlLang.bind('click', function() {
        conLang.css('height', 'auto');
        conLang.toggleClass('active');
      });
    },
    settingLang: function() {
      var item = conLang.find('.language-item a');
      item.on('click', function() {
        var lang = $(this).text();
        $('.languages').find('span.text').text(lang);
        conLang.removeClass('active');
      });
    }
  };
  var instance = {
    init: function(){
      $$.managerClose();
      $$.managerMenu();
      $$.winResize();
      $$.langManager();
      $$.settingLang();
    }
  };
  return instance;

});