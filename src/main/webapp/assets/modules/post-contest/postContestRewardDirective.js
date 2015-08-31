techlooper.directive("postContestReward", function ($http, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/post-contest/postContestReward.html",
    link: function (scope, element, attr, ctrl) {
      $(element).find('.post-contest-field-content').find('[data-toggle="tooltip"]').tooltip({
        html: true,
        placement: 'right',
        'trigger': "focus",
        animation: true
      });
      $(element).find('.post-contest-field-tooltip').find('.ic-tooltip').tooltip({html: true, placement: 'right'});
    }
  }
});