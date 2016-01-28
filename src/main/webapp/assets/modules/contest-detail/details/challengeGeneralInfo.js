techlooper.directive('challengeGeneralInfo', function (localStorageService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/details/challengeGeneralInfo.html",
    link: function (scope, element, attr, ctrl) {
      //var joiningChallengeId = localStorageService.get("joiningChallengeId");
      console.log(scope.contestDetail);
      scope.$internalForm = {visible: scope.contestDetail.$isJoiningChallenge};
      //localStorageService.remove("savedDraftRegistrant");

      scope.toggleJoinInternalForm = function () {
        scope.$internalForm.visible = !scope.$internalForm.visible;
      }

      //scope.signInInternalForm =function(){
      //  $('.sign-internal').modal('show');
      //}
    }
  };
});