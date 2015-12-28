angular.module("Common").factory("loadingBoxFactory", function (jsonValue, utils) {
  utils.registerNotification(jsonValue.notifications.switchScope, function () {
    $('body').addClass('loading');
  });
  utils.registerNotification(jsonValue.notifications.loading, function () {
    $('body').addClass('loading');
  });

  var hide = function () {
    $('body').removeClass('loading');
  };

  utils.registerNotification(jsonValue.notifications.loaded, hide);
  utils.registerNotification(jsonValue.notifications.gotData, hide);
  utils.registerNotification(jsonValue.notifications.hideLoadingBox, hide);
  return {
    initialize: function () {}
  };
});