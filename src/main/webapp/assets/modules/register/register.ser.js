angular.module('Register').factory('registerService', function (shortcutFactory, jsonValue, localStorageService, utils) {
  var scope;
  //var lsKeys = localStorageService.keys();
  //localStorageService.set(key, value);


  var $$ = {
    initialize: function ($scope) {
      scope = $scope;
      $('input[type="checkbox"]').checkbox();
      $("#salary").slider({});
      $('.btn-close').click(function () {shortcutFactory.trigger('esc');});
      $('.btn-logo').click(function () {shortcutFactory.trigger('esc');});
      $('.register-successful').click(function () {shortcutFactory.trigger('esc');});
    },

    enableNotifications: function () {
      return $(".register-contianer").is(":visible");
    }
  }

  var instance = {
    updateUserInfo: function(userInfo) {
      $(".firstName").val(userInfo.firstName);
      $(".lastName").val(userInfo.lastName);
      $(".emailAddress").val(userInfo.emailAddress);
    }
  };

  utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
  return instance;
});