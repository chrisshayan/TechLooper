angular.module("Common").factory("loadingBoxFactory", function (jsonValue, utils, bootstrapTourFactory) {
  utils.registerNotification(jsonValue.notifications.switchScope, function(){
    $('.loading-data').show();
  });
  utils.registerNotification(jsonValue.notifications.gotData, function(){
    $('.loading-data').fadeOut(500);
    bootstrapTourFactory.makeTourGuide();
  });

  return {
    initialize: function(){}
  };
});