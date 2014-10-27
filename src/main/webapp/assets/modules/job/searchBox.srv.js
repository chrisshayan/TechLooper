angular.module("Jobs").factory("searchBoxService", function ($location, jsonValue, utils) {
  var scope;

  var instance = {
    initSearchTextbox: function ($scope, textArray) {
      scope = $scope;
      var tags = utils.setIds(jsonValue.technicalSkill);
      $(".searchText").select2({
        width: "100%",
        tags: tags,
        tokenSeparators: [" "],
        formatSelection: instance.formatItem,
        formatResult: instance.formatItem,
        createSearchChoice: function (text) {
          var tag = utils.findBy(tags, "text", text);
          if (tag === undefined) {
            tag = {id: text, text: text};
          }
          return tag;
        },
        createSearchChoicePosition: "bottom",
        placeholder: "Enter to search...",
        //allowClear: true,
        openOnEnter: false,
        //containerCssClass: "test",
        escapeMarkup: function (markup) { return markup; }
      });

      if (textArray !== undefined) {
        var data = [];
        $.each(textArray, function (i, text) {
          var tag = utils.findBy(tags, "text", text);
          data.push(tag !== undefined ? tag : {id: text, text: text});
        });
        $(".searchText").select2("data", data);
      }

      //var fireChangeEvent = false;
      $('.searchText > ul > li > input.select2-input').on('keyup', function (event) {
        if (event.keyCode === 13) {
          instance.doSearch();
        }
      });
      $(".searchText").on("change", function (event) {
        
        var imgs = $('.technical-Skill-List').find('img');
        imgs.each(function(){
          var title = $(this).attr('title');
          if(event.added  && title == event.added.text){
            $(this).addClass('active');
          }
          if(event.removed  && title == event.removed.text){
            $(this).removeClass('active');
          }

        });
      });

      $('.btn-search').click(instance.doSearch);

      $('.btn-close').click(function(){
        $('body').css("background-color", "#2e272a");
      });
    },

    doSearch: function () {
      $('body').css("background-color", "#eeeeee");
      var tags = $(".searchText").select2("data").map(function (value) {return value.text;});
      $location.path(jsonValue.routerUris.jobsSearch + tags.join());
      scope.$apply();
    },

    formatItem: function (item) {
      if (item.id === item.text) {
        return "<div style='height: 16px;'>" + item.text + "</div>";
      }
      return "<img style='width: 16px; height: 16px;' src='images/" + item.logo + "'> " + item.text + " </img>";
    },

    openSearchForm: function (h) {
      $('.search-block').animate({
        'min-height': h,
        bottom: 0
      }, {
        duration: '10000',
        easing: 'easeOutQuad'
      });
    }
  }

  return instance;
});
