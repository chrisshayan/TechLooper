angular.module('Register').factory('registerService',
  function (shortcutFactory, jsonValue, localStorageService, utils, $http, connectionFactory, $location, $auth,
            $window, $rootScope, $translate) {
    var scope;
    var $$ = {
      initialize: function ($scope) {
        scope = $scope;
        $("#salary").slider({});
        $('.btn-logo').click(function () {
          shortcutFactory.trigger('esc');
        });
        $('.register-successful').click($$.saveUserInfo);
        $$.userInfo();
      },

      saveUserInfo: function (e) {
        $(e.target).unbind("click tap");
        connectionFactory.saveUserInfo(scope.userInfo)
          .then(function (resp) {
            utils.notify(jsonValue.messages.successSave, 'success');
            $location.path("/");
          })
          .catch(function (errors) {
            utils.notify(jsonValue.messages.errorFieldsSave, 'error');
            $.each(errors, function (i, error) {
              // TODO: design error display
              $("." + error.field).css("border", "1px solid red");//error.defaultMessage
            });
          })
          .finally(function () {
            $(e.target).bind("click tap", $$.saveUserInfo);
          });
      },

      enableNotifications: function () {
        return utils.getView() === jsonValue.views.register;
      },

      userInfo: function () {
        if ($rootScope.userInfo === undefined) {
          return false;
        }
        $(".emailAddress").prop("disabled", $rootScope.userInfo.emailAddress != null);

        if (instance.hasProfile(jsonValue.authSource[0])) {
          $("#alreadyRegisteredVietnamworks").show();
          $("#registerVietnamworksQuestion").hide();
          $(".register-vietnamworks").prop("disabled", true);
          $(".register-vietnamworks").prop("checked", true);
        }
        else {
          $("#alreadyRegisteredVietnamworks").hide();
          $("#registerVietnamworksQuestion").show();
        }
      }
    }

    var instance = {
      hasProfile: function (auth) {
        if ($rootScope.userInfo === undefined) {
          return false;
        }
        return $rootScope.userInfo.profileNames.indexOf(auth.provider.toUpperCase()) > -1;
      },

      openOathDialog: function (auth) {
        if (instance.hasProfile(auth)) {
          return false;
        }
        utils.sendNotification(jsonValue.notifications.loading, $(window).height());
        $auth.authenticate(auth.provider)
          .then(function (resp) {//success
            delete $window.localStorage["satellizer_token"];
            $rootScope.userInfo.profileNames.push(auth.provider.toUpperCase());
          })
          .finally(function (resp) {
            utils.sendNotification(jsonValue.notifications.loaded);
          });
      },

      translation: function () {
        $translate("up2perMonth").then(function (translation) {
          scope.salaryOptions = [-800, -1000, -1500, -2000, -2500, -3000, -4000].map(function (val) {
            return {
              label: translation.replace("{}", Math.abs(val)),
              value: val
            };
          });
        });
        $translate("moreThanPerMonth").then(function (translation) {
          scope.salaryOptions.push({
            label: translation.replace("{}", 4000),
            value: 4000
          });
        });
      },

      registerVietnamworks: function () {
        if ($('.register-vietnamworks').prop('checked') === true) {
          $rootScope.userInfo.profileNames.push(jsonValue.authSource[0].provider.toUpperCase());
        } else {
          var i = $rootScope.userInfo.profileNames.indexOf("VIETNAMWORKS");
          if (i != -1) {
            $rootScope.userInfo.profileNames.splice(i, 1);
          }
        }
      }
    };

    utils.registerNotification(jsonValue.notifications.userInfo, $$.userInfo, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.changeLang, instance.translation, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });