techlooper.directive("postContestChallenge", function ($http, utils, jsonValue) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/post-contest/postContestChallenge.html",
    link: function (scope, element, attr, ctrl) {
      $(element).find('[data-toggle="tooltip"]').tooltip({
        html: true,
        placement: 'right',
        'trigger': "focus",
        animation: true
      });
    }
  }
});