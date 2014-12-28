angular.module("Header").controller("headerController", function ($scope, headerService) {
  headerService.initialize();
  //if (localStorageService.get(jsonValue.storage.key)) {
  //  $(".signin-signout-container > a").attr("href", "#/signout").find('span').addClass('signout');
  //}
  //
  //headerService.getChart();
  //$('.btn-setting').click(headerService.showSetting);
  //$("i[techlooper='chartsMenu']").click(headerService.changeChart);
  //headerService.changeChart();
  //headerService.restartTour();
  //$scope.langKeys = jsonValue.availableLanguageKeys;
});
