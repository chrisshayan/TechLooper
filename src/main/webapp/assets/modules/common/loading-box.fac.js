angular.module("Common").factory("loadingBoxFactory", function (jsonValue, utils) {
  utils.registerNotification(jsonValue.notifications.switchScope, function(){
    $('.loading-data').show();
  });
  utils.registerNotification(jsonValue.notifications.gotData, function(){
    $('.loading-data').fadeOut(500);
  });
  return {
    initialize: function(){}
  };
});