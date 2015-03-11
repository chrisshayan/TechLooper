techlooper.controller("tsHeaderController", function ($scope) {
  var menuManagement = (function() {
    var hWin = $(window).height();
    $( window ).resize(function() {
      hWin = $(window).height();
    });
    var ctrlClose = $('.close-menu'),
      ctrlMenu = $('.menu-icon'),
      init = function() {
        managerClose();
        managerMenu();
      },
      managerClose = function() {
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
      managerMenu = function() {
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
      };
    return {
      init: init
    };
  })();
  menuManagement.init();
});
