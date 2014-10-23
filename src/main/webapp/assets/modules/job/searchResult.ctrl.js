angular.module('Jobs').controller('searchResultController',
  function ($scope, $routeParams, connectionFactory, jsonValue, searchBoxService) {
    searchBoxService.initializeIntelligent($scope);

    var skills = $routeParams.text.split(" ");
    $('.selectator_chosen_items').css('display', 'block');
    $.each(skills, function (index, skill) {
      //var items = '<div class="selectator_chosen_item"><div class="selectator_chosen_item_left">';
      //items = items + '<img src="images/lg-' + skill + '.png"></div>';
      //items = items + '<div class="selectator_chosen_item_title">' + skill + '</div>';
      //items = items + '<div class="selectator_chosen_item_remove">X</div><div style="clear: both;"></div></div></div>';
      //$('.selectator_chosen_items').append(items);
      //removeItem(skill);
      $("select.termsList option:contains('Java')").attr("selected", "selected");
      searchBoxService.refresh();
    });
    //function removeItem(skill) {
    //  var list = $('.selectator_options li').find('.selectator_option_title');
    //  $.each(list, function (index, text) {
    //    if (angular.lowercase($(this).text()) == skill) {
    //      $(this).parent().remove();
    //    }
    //  });
    //}

    //transferKeyWords();

});
