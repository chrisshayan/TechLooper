angular.module('Register').factory('registerService',
  function (shortcutFactory, jsonValue, localStorageService, utils, $http, connectionFactory, $location) {
    var scope;
    var flag = {};

    var $$ = {
      initialize: function ($scope) {
        scope = $scope;
        $('input[type="checkbox"]').checkbox();
        $("#salary").slider({});
        $('.btn-close').click(function () {shortcutFactory.trigger('esc');});
        $('.btn-logo').click(function () {shortcutFactory.trigger('esc');});
        $('.register-successful').click($$.saveUserInfo);
      },

      saveUserInfo: function (e) {
        if (flag.saveUserInfo === true) {
          return;
        }
        flag.saveUserInfo = true;
        connectionFactory.saveUserInfo(scope.userInfo)
          .then(function (resp) {
            $location.path("/");
          })
          .catch(function (errors) {
            console.log(errors);
            $.each(errors, function (i, error) {
              // TODO: design error display
              $("input." + error.field).css("border", "1px solid red");//error.defaultMessage
            });
          })
          .finally(function () {
            flag.saveUserInfo = false;
          });
      },

      enableNotifications: function () {
        return $(".register-contianer").is(":visible");
      }
    }

    var instance = {
      //updateUserInfo: function (userInfo) {
      //  $(".firstName").val(userInfo.firstName);
      //  $(".lastName").val(userInfo.lastName);
      //  $(".emailAddress").val(userInfo.emailAddress);
      //}
    };

    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });