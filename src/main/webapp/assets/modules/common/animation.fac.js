angular.module("Common").factory("animationFactory", function (jsonValue, utils) {

  return {
    animatePage: function () {
      switch (utils.getView()) {
        case jsonValue.views.jobsSearch:
          $(window).resize(function () {
            $('.search-block').animate({
              top: 0,
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
              'width': '100%'
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