techlooper.directive('contestList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/contest-list.html",
    link: function (scope, el, attrs) {
    }
  };
}).directive('challengeSearchForm', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/challenge-search-form.html",
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