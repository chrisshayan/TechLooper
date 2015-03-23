techlooper.factory("talentProfileService", function () {
  var $$ = {
    countUp: function($) {
      $$.animateValue("followers-count", 0, 76, 2000);
      $$.animateValue("starred-count", 0, 30, 2500);
      $$.animateValue("following-count", 0, 83, 2500);
      $$.animateValue("repositories-count", 0, 12, 2500);
    },
    animateValue: function(cls, start, end, duration) {
    var range = end - start;
    var current = start;
    var increment = end > start? 1 : -1;
    var stepTime = Math.abs(Math.floor(duration / range));
    var obj = $('.'+ cls);
    var timer = setInterval(function() {
      current += increment;
      obj.text(current);
      if (current == end) {
        clearInterval(timer);
      }
    }, stepTime);
  },
  tabManagerResume: function(){
      $$.activeTab();
    },
    activeTab: function(){
      var tab = $('.resume-tab-manager').find('li');
      tab.on('click', function(){
        var clContent = $(this).attr('data-tab-content');
        tab.removeClass('active');
        $('.resume-content-detail').removeClass('active');
        $(this).addClass('active');
        $('.'+ clContent).addClass('active');
      });
    },
    rankingAnimate: function(){
      $$.addPercent(40);
    },
    addPercent: function(n){
      var item  = $('.ranking'),
          rl = item.find('.real');
      rl.each(function(){
        var per = Math.floor((Math.random() * 100) + 40);
        $(this).animate({
          width: per
        }, 1000);
      });
    }
  };
  var instance = {
    init: function(){
      //$$.tabManagerResume();
      $$.rankingAnimate();
      $$.countUp();
    }
  };

  return instance;

});