techlooper.factory("tsSearchResultService", function () {
  var $$ = {
      talentItemManager: function () {
        var item = $('.talent-item');
        var maxHeight = 0;
        item.each(function(){
          if(maxHeight < $(this).height()){
            maxHeight = $(this).height();
          }
        });
        item.css('height', maxHeight + 20);
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
    },

    updateSearchText: function() {
      //if ($.isArray(textArray)) {
      //  var options = [];
      //  var values = [];
      //  $.each(textArray, function (i, text) {
      //    var tag = utils.findBy(jsonValue.technicalSkill, "text", text);
      //    if (tag === undefined) {
      //      options.push({text: text});
      //      values.push(text);
      //    }
      //    else {
      //      values.push(tag.text);
      //    }
      //  });
      //  searchText.addOption(options);
      //  searchText.setValue(values);
      //}
    }
  };

  return instance;

});