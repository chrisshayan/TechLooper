techlooper.directive("postContestTimeline", function ($http, utils, jsonValue) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/post-contest/postContestTimeline.html",
    link: function (scope, element, attr, ctrl) {
      $(".normal-date[data-toggle='tooltip']").tooltip({
        html: true,
        placement: 'right',
        'trigger': "focus",
        animation: true
      });
      $('.calendar-deadline').find('[data-toggle="tooltip"]').tooltip({
        html: true,
        placement: 'left',
        'trigger': "focus",
        animation: true
      });
      $(element).find('.date').datepicker({
        autoclose:  true,
        format: 'dd/mm/yyyy'
      });
      utils.sendNotification(jsonValue.notifications.loaded);
    }
  }
});