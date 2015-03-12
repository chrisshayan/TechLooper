techlooper.controller("tsHeaderController", function ($scope, tsHeaderService, $timeout, $location, jsonValue) {
  tsHeaderService.init();
  $timeout(function(){
    if($('.search-form-block').length > 0){
      tsHeaderService.menuAnimate();
    }
    tsHeaderService.langManager();
    tsHeaderService.settingLang();
  }, 100);

  $scope.itProfessionalClick = function() {
    $location.path(jsonValue.routerUris.bubble);
  }
});
