techlooper.controller("tsHeaderController", function ($scope, tsHeaderService, $timeout) {
  tsHeaderService.init();
  $timeout(function(){
    tsHeaderService.menuAnimate();
    tsHeaderService.langManager();
    tsHeaderService.settingLang();
  }, 100);
});
