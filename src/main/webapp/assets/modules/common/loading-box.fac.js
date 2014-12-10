angular.module("Common").factory("loadingBoxFactory", function (jsonValue, utils) {
  var dataTour = [];
  utils.registerNotification(jsonValue.notifications.switchScope, function(){
    $('.loading-data').show();
    dataTour = utils.getDataTour();
  });
  utils.registerNotification(jsonValue.notifications.gotData, function(){
    $('.loading-data').fadeOut(500);
    utils.makeTourGuide(dataTour);
  });
  return {
    initialize: function(){}
  };
});