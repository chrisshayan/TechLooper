techlooper.directive('challengeList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/challengeList.html",
    link: function (scope, element, attr, ctrl) {
      scope.toggleJoinInternalForm = function (challenge) {
        if (!challenge.$internalForm) challenge.$internalForm = {};
        challenge.$internalForm.visible = !challenge.$internalForm.visible;
      }
      scope.signInInternalForm =function(){
        $('.sign-internal').modal('show');
      }
    }
  };
})