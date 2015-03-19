techlooper.factory("talentProfileService", function () {
  var $$ = {
    changeScrollStyle: function(){
      var hDiv = $('.talent-info-content') - 40;
      $(".repositories-list").width('100%').height(hDiv);
      $('.repositories-list').perfectScrollbar();
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
      //$$.changeScrollStyle();
      $$.tabManagerResume();
      $$.rankingAnimate();
    }
  };

  return instance;

});