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
          height: '130px',
          width: '130px'
        });
      }).mouseleave(function () {
          $(this).find('.talent-action-block').stop().animate({
            height: 0,
            width: 0
          });
        });
    }
  };
  var instance = {
    init: function () {
      $$.talentItemManager();
      $(".job-info span").dotdotdot({
        height: 23
      });
      $("span.location").dotdotdot({
        height: 23
      });

    },

    updateSearchText: function (request) {
      var searchRequest = tsMainService.getSearchRequest();
      for (var prop in request) {
        var options = [];
        var values = [];
        $.each(request[prop], function (i, text) {
          options.push({text: text});
          values.push(text);
        });
        if (searchRequest[prop] !== undefined) {
          searchRequest[prop].addOption(options);
          searchRequest[prop].setValue(values);
        }
      }
    }
  };

  return instance;

});