techlooper.controller("talentProfileController", function ($timeout, talentProfileService) {
  $timeout(function(){
    talentProfileService.init();
  }, 200);
});
