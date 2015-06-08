techlooper.directive("srNavigation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/salary-report/sr-navigation.tem.html",
    link: function(){
      var windScroll = $(window).scrollTop();
      if ($('.salary-review-block').position().top <= windScroll) {
        $('.navi-step-salary-review').addClass('fixed');
      }
      $(window).scroll(function() {
        windScroll = $(window).scrollTop();
        if (windScroll > 0) {
          if ($('.salary-review-block').position().top <= windScroll) {
            $('.navi-step-salary-review').addClass('fixed');
          } else {
            $('.navi-step-salary-review').removeClass('fixed');
          }
        }
      });
    }
  }
})