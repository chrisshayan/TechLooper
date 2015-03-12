techlooper.controller("tsHeaderController", function ($scope, tsHeaderService, $timeout) {
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
});
