angular.module('Register').factory('registerService',
  function (shortcutFactory, jsonValue, localStorageService, utils, $http, connectionFactory, $location, $auth,
            $window, $rootScope, $translate) {
    var scope;
    var flag = {};

    var $$ = {
      initialize: function ($scope) {
        scope = $scope;

        $('input[type="checkbox"]').checkbox();
        $("#salary").slider({});
        $('.btn-close').click(function () {
          shortcutFactory.trigger('esc');
          $('.navi-container').find('a.sign-out-sign-in').parent().removeClass('active');
          $('.navi-container').find('a.m-chart').parent().addClass('active');
        });
        $('.btn-logo').click(function () {
          shortcutFactory.trigger('esc');
          $('.navi-container').find('a.sign-out-sign-in').parent().removeClass('active');
          $('.navi-container').find('a.m-chart').parent().addClass('active');
        });
        $('.register-successful').click($$.saveUserInfo);
      },

      saveUserInfo: function (e) {
        if (flag.saveUserInfo === true) {
          return;
        }
        flag.saveUserInfo = true;
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
            flag.saveUserInfo = false;
          });
      },

      enableNotifications: function () {
        return utils.getView() === jsonValue.views.register;
      }
    }

    var instance = {
      hasProfile: function (auth) {
        if ($rootScope.userInfo === undefined) {
          return false;
        }
        return $rootScope.userInfo.profileNames.indexOf(auth.provider.toUpperCase()) > -1;
      },

      //salaryOptions: function () {
      //  scope.salaryOptions = [-800, -1000, -1500, -2000, -2500, -3000, -4000].map(function (val) {
      //    return {
      //      label: "Up to $" + Math.abs(val) + " per month",
      //      value: val
      //    };
      //  });
      //  scope.salaryOptions.push({
      //    label: "More than $4000 per month",
      //    value: 4000
      //  });
      //},

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

      translation: function() {
        $translate("up2perMonth").then(function (translation) {
          scope.salaryOptions = [-800, -1000, -1500, -2000, -2500, -3000, -4000].map(function (val) {
            return {
              label: translation.replace("{}", val),
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

    utils.registerNotification(jsonValue.notifications.changeLang, instance.translation, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });