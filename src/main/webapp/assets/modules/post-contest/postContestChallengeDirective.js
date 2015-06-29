techlooper.directive("postContestChallenge", function ($http, utils) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/post-contest/postContestChallenge.html",
    link: function (scope, element, attr, ctrl) {
      $('[data-toggle="tooltip"]').tooltip({html: true, placement: 'right'});

    }
  }
});