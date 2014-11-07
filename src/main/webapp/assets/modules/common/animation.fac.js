angular.module("Common").factory("animationFactory", function (jsonValue, utils) {

  return {
    animatePage: function () {
      switch (utils.getView()) {
        case jsonValue.views.jobsSearch:
          $(window).resize(function () {
            $('.search-block').animate({
              'min-height': $(window).height(),
              bottom: 0
            }, {
              duration: '10000',
              easing: 'easeOutQuad'
            });
          });
          $(window).resize();
          break

        case jsonValue.views.analyticsSkill:
          $(window).resize(function() {
            $('.technical-detail-page').animate({
              'min-height': $(window).height(),
              'width': '100%',
              top: 0,
              botom: 0,
              left: 0,
              position: 'fixed'
            }, {
              duration: '10000',
              easing: 'easeOutQuad'
            });
          });
          $(window).resize();
          break

      }
    }
  };
});