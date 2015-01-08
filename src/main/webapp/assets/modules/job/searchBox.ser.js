angular.module("Jobs").factory("searchBoxService", function ($location, jsonValue, utils, $translate, shortcutFactory, navigationService) {
  var scope, searchText, textArray;

  var $$ = {
    translation: function() {
      $translate("searchTextPlaceholder").then(function (translation) {
        searchText.setPlaceholder(translation);
      });
    },

    doSearch: function () {
      // TODO: #1 - change the body background to white
      $('body').css("background-color", "#eeeeee");
      $location.path(jsonValue.routerUris.jobsSearch + "/" + searchText.getValue());
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
    },

    enableNotifications: function () {
      return $('.searchText').is(":visible");
    },

    initializeSearchTextbox: function ($scope, $textArray) {
      $('.search-block').css('min-height', $(window).height() - 5);
      utils.sendNotification(jsonValue.notifications.gotData);
      scope = $scope;
      textArray = $textArray;

      searchText = $('.searchText').selectize({
        plugins: {
          "remove_button": {},
          "restore_on_backspace": {},
          "techlooper": {onReturn: $$.doSearch}
        },
        sortField: "text",
        mode: "multi",
        persist: false,
        createOnBlur: false,
        create: true,
        options: jsonValue.technicalSkill,
        valueField: "text",
        searchField: ['text'],
        labelField: "text",
        //placeholder: "Enter to search...",
        createFilter: function (input) {
          var ok = true;
          $.each(this.options, function (index, value) {
            if (value.text.toLowerCase() === input.toLowerCase()) {
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

      $$.translation();

      if ($.isArray(textArray)) {
        var options = [];
        var values = [];
        $.each(textArray, function (i, text) {
          var tag = utils.findBy(jsonValue.technicalSkill, "text", text);
          if (tag === undefined) {
            options.push({text: text});
            values.push(text);
          }
          else {
            values.push(tag.text);
          }
        });
        searchText.addOption(options);
        searchText.setValue(values);
      }

      $('.btn-search').click($$.doSearch);

      $('.btn-close').click(function () {
        shortcutFactory.trigger('esc');
        var view = utils.getView();
        if(view == jsonValue.views.pieChart || view == jsonValue.views.bubbleChart){
          navigationService.restoreNaviStyle();
        }
      });
      $('.btn-logo').click(function () {
        shortcutFactory.trigger('esc');
        var view = utils.getView();
        if(view == jsonValue.views.pieChart || view == jsonValue.views.bubbleChart){
          navigationService.restoreNaviStyle();
        }
      });

      $('.btn-search').css({
        'height': $('.selectize-control').height() - 9,
        'line-height': ($('.selectize-control').height() - 9) + 'px'
      });
      searchText.focusNoDropdown();
      $$.alignButtonSearch();
    }
  }

  var instance = {
    getSearchText: function () {
      return searchText;
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
    },

    updateNavi: function(){
      $('.navi-container').find('li').removeClass('active');
      $('.navi-container').find('a.m-search-jobs').parent().addClass('active');
      $('.main-navi-block').css('background','url(images/line-h1.png) #ccc right top repeat-y');

    }
  }

  utils.registerNotification(jsonValue.notifications.switchScope, $$.initializeSearchTextbox, $$.enableNotifications);
  utils.registerNotification(jsonValue.notifications.defaultAction, $$.doSearch, $$.enableNotifications);
  utils.registerNotification(jsonValue.notifications.changeLang, $$.translation, $$.enableNotifications);

  return instance;
});
