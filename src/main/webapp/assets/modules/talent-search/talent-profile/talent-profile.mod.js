techlooper.directive("generalInfo", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/general-info.tem.html",
    link: function(scope, element, attrs) {
      if(history.length < 2){
        $('.back-search-talent-page').hide();
      }
      $('.back-search-talent-page').on('click', function() {
        if(history.length > 1){
          history.back();
          scope.$apply();
        }
      });
    }
  }
}).directive("evaluation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/evaluation.tem.html"
  }
}).directive("resume", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/resume.tem.html"
  }
});