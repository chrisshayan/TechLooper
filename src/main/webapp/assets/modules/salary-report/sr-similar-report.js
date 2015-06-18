techlooper.directive("srSimilarReport", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-similar-report.tem.html",
    link: function (scope) {
      scope.unlimited = 5;
      scope.skillsReport = [
        {"name": "Javascript"},
        {"name": "Business Development"},
        {"name": "Problem Management"},
        {"name": "Maintain Hardware and Software Budgets"},
        {"name": "Maintain Hardware and Software Budgets"},
        {"name": "Javascript"},
        {"name": "Business Development"},
        {"name": "Problem Management"},
        {"name": "Javascript"},
        {"name": "Business Development"},
        {"name": "Problem Management"},
        {"name": "Maintain Hardware and Software Budgets"},
        {"name": "Maintain Hardware and Software Budgets"},
        {"name": "Javascript"},
        {"name": "Business Development"},
        {"name": "Problem Management"}
      ];
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