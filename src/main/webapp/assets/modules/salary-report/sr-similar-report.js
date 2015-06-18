techlooper.directive("srSimilarReport", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-similar-report.tem.html",
    link: function (scope) {
      scope.unlimited = 5;
      scope.showMoreSkill =function(){
        if($('.right-content').find('a').hasClass('more')){
          $('.right-content').find('a').removeClass('more');
          scope.unlimited = 40;
        }else{
          $('.right-content').find('a').addClass('more');
          scope.unlimited = 5;
        }
      }
    }
  }
});