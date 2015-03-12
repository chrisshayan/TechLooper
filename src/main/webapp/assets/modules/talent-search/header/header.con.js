techlooper.controller("tsHeaderController", function ($scope, tsHeaderService, $timeout) {
  tsHeaderService.init();
  $timeout(function(){
    tsHeaderService.menuAnimate();
  }, 100);
});
