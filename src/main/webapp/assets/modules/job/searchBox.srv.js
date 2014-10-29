angular.module("Jobs").factory("searchBoxService", function ($location, jsonValue, utils, shortcutFactory) {
  var scope, searchText;

  var $$ = {
    preventEsc: function() {
      var isVideoShown = $("#companyVideoInfor").is(":visible");;
      return isVideoShown;
    },


    doSearch: function () {
      // TODO: change body background
      $('body').css("background-color", "#eeeeee");
      $location.path(jsonValue.routerUris.jobsSearch + searchText.getValue());
      scope.$apply();
    },

    pressEsc: function (event) {
      if ($$.preventEsc()) {
        console.log("prevent from service");
        return false;
      }
      utils.popHistory() === undefined ? $location.path("/") : history.back();
      scope.$apply();
      return true;
    },

    alignButtonSearch: function () {
      searchText.on("item_add", function (value, item) {
        $('.btn-search').css({
          'height': $('.selectize-control').height() - 9,
          'line-height': ($('.selectize-control').height() - 9) + 'px'
        });
      });
      searchText.on("item_remove", function (value) {
        $('.btn-search').css({
          'height': $('.selectize-control').height() - 9,
          'line-height': ($('.selectize-control').height() - 9) + 'px'
        });
      });
    }
  }

  var instance = {
    initSearchTextbox: function ($scope, textArray) {
      scope = $scope;
      searchText = $('.searchText').selectize({
        plugins: {
          "remove_button": {},
          "restore_on_backspace": {},
          "techlooper": {onReturn: $$.doSearch}
        },
        mode: "multi",
        persist: false,
        createOnBlur: false,
        create: true,
        options: jsonValue.technicalSkill,
        valueField: "text",
        searchField: ['text'],
        labelField: "text",
        placeholder: "Enter to search...",
        createFilter: function (input) {
          var ok = true;
          $.each(this.options, function (index, value) {
            if (value.text.toLowerCase() === input) {
              ok = false;
              return false;
            }
          });
          return ok;
        },
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

      if ($.isArray(textArray)) {
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

      $('.btn-search').click($$.doSearch);

      $('.btn-close').click($$.pressEsc);

      $('.btn-search').css({
        'height': $('.selectize-control').height() - 9,
        'line-height': ($('.selectize-control').height() - 9) + 'px'
      });

      $$.alignButtonSearch();
      shortcutFactory.initialize([{key: "esc", fn: $$.pressEsc}]);
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
    changeBodyColor: function () {
      var url = jsonValue.routerUris.jobsSearch + searchText.getValue();
      if ($location.path() == url) {
        $('body').css("background-color", "#eeeeee");
      }
    },
    hightlightSKill: function () {
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
    }
  }

  return instance;
});
