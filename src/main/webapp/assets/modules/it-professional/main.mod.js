techlooper.directive("itProfessional", function ($compile, $location, utils, jsonValue) {

  return {
    restrict: "A",
    templateUrl: function (elem, attrs) {
      switch (utils.getView()) {
        case jsonValue.views.bubbleChart:
          return "modules/bubble-chart/bubble-chart.tem.html";

        case jsonValue.views.pieChart:
          //if (utils.isMobile()) {
          //  return "modules/pie-chart/pie-chart-mobile.tem.html";
          //}
          return "modules/pie-chart/pie-chart.tem.html";

        case jsonValue.views.jobsSearch:
          return "modules/job/searchForm.tem.html";

        case jsonValue.views.jobsSearchText:
          return "modules/job/searchResult.tem.html";

        case jsonValue.views.analyticsSkill:
          return "modules/skill-analytics/skill-analytics.tem.html";

        case jsonValue.views.signIn:
          return "modules/signin/signin.tem.html";

        case jsonValue.views.register:
          return "modules/register/register.tem.html";

        case jsonValue.views.landing:
          return "modules/landing/landing.tem.html";
      }
      return '';
    },
    link: function(){
      $('.js-footer').removeClass('technical-detail');
    }
  };
});