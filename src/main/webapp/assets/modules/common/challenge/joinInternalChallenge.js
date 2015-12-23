techlooper.directive("joinInternalChallenge", function (apiService) {
  return {
    restrict: "E",
    replace: true,
    scope: {
      challenge: "=",
      cancel: "="
    },
    templateUrl: "modules/common/challenge/joinInternalChallenge.html",
    link: function (scope, el, attrs) {
      //scope.form = {};
      //scope.form.showJoinInternalForm = function () {
      //  scope.visibleJoinInternalForm = true;
      //  apiService.joinInternalChallenge();
      //}
      //scope.form.hideJoinInternalForm = function () {
      //  delete scope.visibleJoinInternalForm;
      //  delete scope.submission.submissionURL;
      //  delete scope.submission.submissionDescription;
      //}
    }
  }
});