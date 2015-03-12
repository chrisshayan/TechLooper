techlooper.controller("tsHeaderController", function ($scope, tsHeaderService, $timeout, $location, jsonValue) {
  tsHeaderService.init();
  $timeout(function(){
    if($('.main-banner').length > 0){
      tsHeaderService.menuAnimate();
    }else{
      $('header').addClass('changed');
    }
    tsHeaderService.langManager();
    tsHeaderService.settingLang();
  }, 100);

  $scope.itProfessionalClick = function() {
    $location.path(jsonValue.routerUris.bubble);
  }
});
