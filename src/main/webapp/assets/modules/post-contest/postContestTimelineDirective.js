techlooper.directive("postContestTimeline", function ($http, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/post-contest/postContestTimeline.html",
    link: function (scope, element, attr, ctrl) {
      $(element).find('.date').datepicker({
        autoclose:  true,
        format: 'dd/mm/yyyy'
      });
    }
  }
});