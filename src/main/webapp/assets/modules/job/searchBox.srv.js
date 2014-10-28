angular.module("Jobs").factory("searchBoxService", function ($location, jsonValue, utils) {
  var scope, searchText;

  var instance = {
    initSearchTextbox: function ($scope, textArray) {
      scope = $scope;
      searchText = $('.searchText').selectize({
        plugins: ['remove_button', "restore_on_backspace"],
        persist: false,
        createOnBlur: false,
        create: true,
        options: jsonValue.technicalSkill,
        valueField: "text",
        searchField: ['text'],
        labelField: "text",
        placeholder: "Enter to search...",
        render: {
          item: function (item, escape) {
            var img = item.logo === undefined ? "" : "<img style='width: 16px; height: 16px;' src='images/" + item.logo + "'/> ";
            return "<div>" + img + item.text + " </div>";
          },
          option: function (item, escape) {
            var img = item.logo === undefined ? "" : "<img style='width: 16px; height: 16px;' src='images/" + item.logo + "'/> ";
            return "<div>" + img + item.text + " </div>";
          }
        }
      })[0].selectize;

      if (textArray !== undefined) {
        var options = [];
        $.each(textArray, function (i, text) {
          var tag = utils.findBy(jsonValue.technicalSkill, "text", text);
          if (tag === undefined) {
            options.push({text: text});
          }
        });
        searchText.addOption(options);
        searchText.setValue(textArray);
      }

      $( "div.searchText > div.selectize-input > input[type=text]").on('keyup', function (event) {
        if (event.keyCode === 13) {
          if (!$( "div.searchText .selectize-dropdown").is(":visible")) {
            instance.doSearch();
          }
        }
      });

      searchText.on("item_add", function (value, item) {
        $('.technical-Skill-List').find('img').each(function () {
          var title = $(this).attr('title');
          if (title === value) {
            $(this).addClass('active');
            return false;
          }
        });
      });

      searchText.on("item_remove", function (value) {
        $('.technical-Skill-List').find('img').each(function () {
          var title = $(this).attr('title');
          if (title === value) {
            $(this).removeClass('active');
            return false;
          }
        });
      });

      searchText.on("dropdown_close", function (dropdown) {
        lastEvent["27"] = dropdown;
      });

      $('.btn-search').click(instance.doSearch);

      eventHandler["27"] = function() {
        $('.btn-close').click();
      }

      $('.btn-close').click(function () {
        $('body').css("background-color", "#2e272a");
      });
    },

    doSearch: function () {
      $('body').css("background-color", "#eeeeee");
      var tags = $(".searchText").select2("data").map(function (value) {return value.text;});
      $location.path(jsonValue.routerUris.jobsSearch + searchText.getValue());
      scope.$apply();
    },

    openSearchForm: function (h) {
      $('.search-block').animate({
        'min-height': h,
        bottom: 0
      }, {
        duration: '10000',
        easing: 'easeOutQuad'
      });
    },
    changeBodyColor: function(){
      var tags = $(".searchText").select2("data").map(function (value) {return value.text;});
      var url = jsonValue.routerUris.jobsSearch + tags.join();
      if($location.path() == url){
        $('body').css("background-color", "#eeeeee");
      }
    }
    //hightlightSKill: function(){
    //  $(".searchText").on("change", function (event) {
    //    var imgs = $('.technical-Skill-List').find('img');
    //    imgs.each(function(){
    //      var title = $(this).attr('title');
    //      if(event.added  && title == event.added.text){
    //        $(this).addClass('active');
    //      }
    //      if(event.removed  && title == event.removed.text){
    //        $(this).removeClass('active');
    //      }
    //    });
    //  });
    //},
    //alignButtonSeatch: function(){
    //  $(".searchText").on("change", function (event) {
    //    $('.btn-search').css({
    //      'height': $('.select2-choices').height(),
    //      'line-height': $('.select2-choices').height() +'px'
    //    });
    //  });
    //}
  }

  return instance;
});
