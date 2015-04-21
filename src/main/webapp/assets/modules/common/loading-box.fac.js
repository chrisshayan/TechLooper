angular.module("Common").factory("loadingBoxFactory", function (jsonValue, utils) {
  utils.registerNotification(jsonValue.notifications.switchScope, function () {
    $('.loading-data').css("height", "auto");
    $('body').addClass('noscroll');
    $('.loading-data').show();
  });
  utils.registerNotification(jsonValue.notifications.loading, function (h) {
    (typeof h === "number") && $('.loading-data').height(h);
    $('.loading-data').show();
  });

  var hide = function () {
    $('.loading-data').fadeOut(500);
    $('body').removeClass('noscroll');
  };
  utils.registerNotification(jsonValue.notifications.loaded, hide);
  utils.registerNotification(jsonValue.notifications.gotData, hide);
  utils.registerNotification(jsonValue.notifications.hideLoadingBox, hide);
  return {
    initialize: function () {}
  };
});