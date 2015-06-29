techlooper.directive("postContestTimeline", function ($http, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/post-contest/postContestTimeline.html",
    link: function (scope, element, attr, ctrl) {
      var date = moment().format('DD/MM/YYYY');
      scope.startDateEx = moment().add(7, 'day').format('DD/MM/YYYY');
      scope.registerDateEx =  moment().add(21, 'day').format('DD/MM/YYYY');
      scope.submitDateEx =  moment().add(63, 'day').format('DD/MM/YYYY');
      $(element).find('.date').datepicker({
        autoclose:  true,
        format: 'dd/mm/yyyy'
      });
    }
  }
});