angular.module("Jobs").factory("searchBoxService", ["$location", "jsonValue", function ($location, jsonValue) {
  var scope;


  return {
    initializeIntelligent: function ($scope) {
      scope = $scope;
      var hWin = $(window).height();
      var keyWords = '';
      openSearchForm($(window).height());
      $(window).resize(function () {
        openSearchForm($(window).height());
      });

      function openSearchForm(h) {
        $('.search-block').animate({
          'height': h,
          bottom: 0
        }, {
          duration: '10000',
          easing: 'easeOutQuad'
        });
      }

      $('.btn-close').click(function () {
        $('.search-block').animate({
          'height': 0,
          'bottom': '50%'
        }, {
          duration: '10000',
          easing: 'easeOutQuad'
        });
        $('body').css("background-color", "#2e272a");
      });

      // load data for auto dropdown list and show technical skill
      var dataSkill = jsonValue.technicalSkill,
        options = '';
      $.each(dataSkill, function (index, skill) {
        options = options + '<option value="' + index + '" data-left="<img src=images/' + skill.logo + '>">' + skill.name + '</option>';
      });
      $('.termsList').append(options);

      var select = $('.termsList');
      select.selectator({
        showAllOptionsOnFocus: true,
        $selectorsList : $('.technical-Skill-List ul')
      });
      $('.btn-search').click(function () {
        if (!$('.selectator_chosen_items').is(':empty')) {
          keyWords = "";
          getKeyWords();
          if (keyWords != '') {
            $location.path("/jobs/search/" + keyWords);
            scope.$apply();
          }
        }
      });

      function getKeyWords() {
        var $this = $('.selectator_chosen_items').find('.selectator_chosen_item_title');
        $this.each(function () {
          keyWords = keyWords + $(this).text() + ' ';
        });
        keyWords = keyWords + $('input.selectator_input').val();
        return keyWords;
      }
    }
  }
}]);
