angular.module('Skill').controller('skillAnalyticsController', function($scope, skillFactory) {

  initTechnicalPage($(window).height());
  $(window).resize(function () {
    initTechnicalPage($(window).height());
  });

  function initTechnicalPage(hWin, wWin) {
    $('.technical-detail-page').animate({
      'height': hWin,
      'width': '100%',
      top: 0,
      botom: 0,
      left: 0,
      position: 'fixed'
    }, {
      duration: '10000',
      easing: 'easeOutQuad'
    });
  }
  skillFactory.drawCircle($scope);
});