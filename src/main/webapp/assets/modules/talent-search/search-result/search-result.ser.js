techlooper.factory("tsSearchResultService", function () {
  var $$ = {
      talentItemManager: function () {
        var item = $('.talent-item');
        item.mouseenter(function () {
          $(this).find('.talent-action-block').stop().animate({
            height: '130px'
          });
        }).mouseleave(function () {
          $(this).find('.talent-action-block').stop().animate({
            height: 0
          });
        });
      }
  };
  var instance = {
    init: function(){
      $$.talentItemManager();
    }
  };

  return instance;

});