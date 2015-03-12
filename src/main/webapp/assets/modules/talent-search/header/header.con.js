techlooper.controller("tsHeaderController", function ($scope, tsHeaderService, $timeout) {
  tsHeaderService.init();
  $timeout(function(){
    if($('.search-form-block').length > 0){
      tsHeaderService.menuAnimate();
    }
    tsHeaderService.langManager();
    tsHeaderService.settingLang();
  }, 100);
});
