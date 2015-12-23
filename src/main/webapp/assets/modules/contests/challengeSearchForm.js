techlooper.directive('challengeSearchForm', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/challengeSearchForm.html",
    link: function (scope, el, attrs) {
      $(document).ready(function(e){
        $('.search-panel .dropdown-menu').find('a').click(function(e) {
          e.preventDefault();
          var param = $(this).attr("href").replace("#","");
          var concept = $(this).text();
          $('.search-panel span#search_concept').text(concept);
          $('.input-group #search_param').val(param);
        });
      });
    }
  };
});