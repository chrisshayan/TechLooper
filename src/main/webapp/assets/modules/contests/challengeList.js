techlooper.directive('challengeList', function ($compile) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/challengeList.html",
    link: function (scope, element, attr, ctrl) {
      //var challengeId = localStorageService.get("joiningChallengeId");
      //scope.$joiningChallengeId = challengeId;
      //var challenge = _.findWhere(scope.contestsList, {challengeId: challengeId});
      //console.log(challenge);

      //scope.challenge.$internalForm = {visible: true};

      //var joinInternalFormHtml = '<join-internal-challenge challenge="$challenge" cancel="hideInternalForm"></join-internal-challenge>';
      //scope.initInternalForm = function(contest) {
      //  if (!contest.$isJoiningChallenge) {
      //    return false;
      //  }
      //
      //  //$("[class^='internal-form-']").html("");
      //  console.log($(".internal-form-" + contest.challengeId).html());
      //  //$(".internal-form-" + contest.challengeId).html("");
      //  //scope.$challenge = contest;
      //  //var compiled = $compile(joinInternalFormHtml)(scope);
      //  //$(".internal-form-" + contest.challengeId).html(compiled);
      //  //challengeItem.find('.challenge-toggle-submissions').html(compiled);
      //  //challengeItem.find('.submission-col .fa-caret-down').addClass('fa-caret-up');
      //  return true;
      //}

      scope.hideInternalForm = function (challenge) {
        challenge.$isJoiningChallenge = false;
        //$(".internal-form-" + challenge.challengeId).html("");
        //if (!challenge.$internalForm) challenge.$internalForm = {};
        //challenge.$internalForm.visible = !challenge.$internalForm.visible;
      }
      //scope.signInInternalForm =function(){
      //  $('.sign-internal').modal('show');
      //}
    }
  };
})