techlooper.factory("talentProfileService", function (jsonValue) {
  var $$ = {
    countUp: function() {
      var flr = 'followers-count',
          flw = 'following-count',
          rep = 'repositories-count',
          num = $('.'+flr).text().trim(),
          st1 = parseInt(num),
          st2 = $('.'+ flw).text().trim(),
          st3 = $('.'+ rep).text().trim();
      $$.animateValue(flr, 0, st1, 2000);
      $$.animateValue(flw, 0, st2, 2500);
      $$.animateValue(rep, 0, st3, 2500);
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
      //var tab = $('.resume-tab-manager').find('li');
      //tab.on('click', function(){
      //  var clContent = $(this).attr('data-tab-content');
      //  tab.removeClass('active');
      //  $('.resume-content-detail').removeClass('active');
      //  $(this).addClass('active');
      //  $('.'+ clContent).addClass('active');
      //});
    },
    rankingAnimate: function(){
      $$.addPercent(80);
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
      //$$.rankingAnimate();
      //$$.countUp();
    },
    showRating: function(n){
      var start ="",
          temp1 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp2 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star-half-full'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp3 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-sta'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp4 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star-half-full'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp5 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star-o'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp6 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star-half-full'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp7 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star-o'></i></li>",
          temp8 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star-half-full'></i></li>",
          temp9 = "<li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li><li><i class='fa fa-star'></i></li>";
      switch(n) {
        case 1:
          start = temp1;
          break;
        case 1.5:
          start = temp2;
          break;
        case 2:
          start = temp3;
          break;
        case 2.5:
          start = temp4;
          break;
        case 3:
          start = temp5;
          break;
        case 3.5:
          start = temp6;
          break;
        case 4:
          start = temp7;
          break;
        case 4.5:
          start = temp8;
          break;
        case 5:
          start = temp9;
          break;
        default:
          start = '';
      }
      $('.rating-status').append(start);
    },
    getLogo: function(url){
      var logo = '';
      $.each(jsonValue.technicalSkill,function(index, value){
        if(url.toLowerCase() == value.text.toLowerCase()){
          logo = value.logo;
        }
      });
      return logo
    },
    getColorProcess: function(){
      
    },
    getPercentRank: function(){

    }
  };

  return instance;

});