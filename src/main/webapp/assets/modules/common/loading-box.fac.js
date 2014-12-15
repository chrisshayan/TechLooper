angular.module("Common").factory("loadingBoxFactory", function (jsonValue, utils) {
  utils.registerNotification(jsonValue.notifications.switchScope, function () {
    $('.loading-data').css("height", "auto");
    $('.loading-data').show();
  });
  utils.registerNotification(jsonValue.notifications.loading, function (h) {
    $('.loading-data').height(h);
    $('.loading-data').show();
  });

  var hide = function () {$('.loading-data').fadeOut(500);};
  utils.registerNotification(jsonValue.notifications.loaded, hide);
  utils.registerNotification(jsonValue.notifications.gotData, hide);
  return {
    initialize: function () {}
  };
});