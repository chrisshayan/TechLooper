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
        //initSelection: function(element, callback) {
        //  var data = [];
        //  $.each(textArray, function(i, text) {
        //    $.each(tags, function(j, tag) {
        //      if (tag.text === text) {
        //        data.push(tag);
        //        return false;
        //      }
        //    });
        //    if (data.length === textArray.length) {
        //      return false;
        //    }
        //  });
        //  callback(data);
        //},
        createSearchChoice: function (text) {
          var tag = utils.findBy(tags, "text", text);
          if (tag === undefined) {
            tag = {id: text, text: text};
          }
          // return a csv string (ids)
          //http://ivaynberg.github.io/select2/#doc-tokenSeparators
          //console.log(select2.val());
          //console.log($(".searchText").select2("data"));
          return tag;
        },
        createSearchChoicePosition: "bottom",
        placeholder: "Enter to search...",
        //allowClear: true,
        openOnEnter: false,
        containerCssClass: "test",
        escapeMarkup: function (markup) { return markup; }
      });

      if (textArray !== undefined) {

        var data = [];
        $.each(textArray, function (i, text) {
          var tag = utils.findBy(tags, "text", text);
          data.push(tag !== undefined ? tag : {id: text, text: text});
        });

        $(".searchText").select2("data", data);
        //console.log($(".searchText"));
      }

      //var fireChangeEvent = false;
      $('.searchText > ul > li > input.select2-input').on('keyup', function (event) {
        if (event.keyCode === 13) {
          //if (!fireChangeEvent) { // Enter event (not from changeEvent)
            instance.doSearch();
          //}
          //fireChangeEvent = false;
        }
      });
      $(".searchText").on("change", function (event) {
        //fireChangeEvent = true;
        //Support highlight selected term
        //console.log(event.val);
        console.log(event.added);
        console.log(event.removed);
      });

      $('.btn-search').click(instance.doSearch);
    },

    doSearch: function () {
      var tags = $(".searchText").select2("data").map(function (value) {return value.text;});
      $location.path("/jobs/search/" + tags.join());
      scope.$apply();
    },

    formatItem: function (item) {
      if (item.id === item.text) {
        return "<div style='height: 16px;'>" + item.text + "</div>";
      }
      return "<img style='width: 16px; height: 16px;' src='images/" + item.logo + "'> " + item.text + " </img>";
    },

    //refresh : function() {
    //  selectBox.refresh();
    //},

    //initializeIntelligent: function ($scope) {
    //  scope = $scope;
    //  var hWin = $(window).height();
    //  var keyWords = '';
    //  var txtInput = '';
    //  $('.btn-close').click(function () {
    //    $('.search-block').animate({
    //      'height': 0,
    //      'bottom': '50%'
    //    }, {
    //      duration: '10000',
    //      easing: 'easeOutQuad'
    //    });
    //    $('body').css("background-color", "#2e272a");
    //  });
    //
    //  // load data for auto dropdown list and show technical skill
    //  var dataSkill = jsonValue.technicalSkill,
    //    options = '';
    //  $.each(dataSkill, function (index, skill) {
    //    options = options + '<option value="' + index + '" data-left="<img src=images/' + skill.logo + '>">' + skill.name + '</option>';
    //  });
    //  $('.termsList').append(options);
    //
    //  var select = $('.termsList');
    //  selectBox = select.selectator({
    //    showAllOptionsOnFocus: true,
    //    $selectorsList: $('.technical-Skill-List ul')
    //  });
    //  $('.search-form').click(function () {
    //    if (!$('.selectator_chosen_items').is(':empty') || $('input.selectator_input').val() != '') {
    //      keyWords = "";
    //      $('body').css("background-color", "#fff");
    //      getKeyWords();
    //      if (keyWords != '') {
    //        $location.path("/jobs/search/" + keyWords);
    //        scope.$apply();
    //        $('input.selectator_input').val(txtInput).css('width', 'auto');
    //      }
    //    }
    //  });
    //  $('.selectator_input').bind('keyup', function (e) {
    //    if (e.keyCode == 13 && !$('.selectator_chosen_items').is(':empty') && $('.selectator').hasClass('options-visible') == false) {
    //      keyWords = "";
    //      getKeyWords();
    //      $('body').css("background-color", "#fff");
    //      if (keyWords != '') {
    //        $location.path("/jobs/search/" + keyWords);
    //        scope.$apply();
    //        $('input.selectator_input').val(txtInput).css('width', 'auto');
    //      }
    //    }
    //  });
    //  function getKeyWords() {
    //    var $this = $('.selectator_chosen_items').find('.selectator_chosen_item_title');
    //    $this.each(function () {
    //      keyWords = keyWords + ' ' + $(this).text();
    //    });
    //    txtInput = $('input.selectator_input').val();
    //    if(txtInput != ''){
    //      keyWords = keyWords + ' ' + txtInput;
    //    }
    //    keyWords = keyWords.substring(1);
    //    return keyWords;
    //  }
    //},
    openSearchForm: function (h) {
      $('.search-block').animate({
        'height': h,
        bottom: 0
      }, {
        duration: '10000',
        easing: 'easeOutQuad'
      });
    }
  }

  return instance;
});
