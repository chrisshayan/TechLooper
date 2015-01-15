angular.module('Register').factory('registerService',
  function (shortcutFactory, jsonValue, localStorageService, utils, $http, connectionFactory, $location, $auth,
            $window, $rootScope, $translate, navigationService) {
    var scope;
    var $$ = {
      initialize: function ($scope) {
        scope = $scope;
        $("#salary").slider({});
        $('.btn-close').click(function () {
          shortcutFactory.trigger('esc');
          navigationService.backtoSearchPage('m-user-profile');
        });

        $('.btn-logo').click(function () {
          shortcutFactory.trigger('esc');
          navigationService.backtoSearchPage('m-user-profile');
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
        if ($rootScope.userInfo === undefined) { return false; }
        $(".emailAddress").prop("disabled", $rootScope.userInfo.emailAddress != null);
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
        if (instance.hasProfile(auth)) { return false; }
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
      }
    };

    utils.registerNotification(jsonValue.notifications.userInfo, $$.userInfo, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.changeLang, instance.translation, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });