techlooper.factory("tsSearchResultService", function (tsMainService) {
  var $$ = {
    talentItemManager: function () {
      var item = $('.talent-item');
      var maxHeight = 0;
      item.each(function () {
        if (maxHeight < $(this).height()) {
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
    init: function () {
      $$.talentItemManager();
    },

    updateSearchText: function (request) {
      var options = [];
      var values = [];
      var skills = tsMainService.getSkills();
      $.each(request.skills, function (i, skill) {
        options.push(skill);
        values.push(skill);
      });
      skills.addOption(options.join(","));
      skills.setValue(values.join(","));
    }
  };

  return instance;

});