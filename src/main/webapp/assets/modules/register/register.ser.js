angular.module('Register').factory('registerService',
  function (shortcutFactory, jsonValue, localStorageService, utils, $http,
            connectionFactory, $location, $auth, $window, $rootScope) {
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
            //console.log(errors);
            utils.notify(jsonValue.messages.errorFieldsSave, 'error');
            $.each(errors, function (i, error) {
              // TODO: design error display
              $("." + error.field).css("border", "1px solid red");//error.defaultMessage
            });
            //$.notify("Please correct the marked field(s) above.", {
            //  className: "error",
            //  autoHideDelay: 3000,
            //  globalPosition: 'bottom right'
            //});
          })
          .finally(function () {
            flag.saveUserInfo = false;
          });
          utils.hideNavigationBar();
      },

      enableNotifications: function () {
        return $(".register-contianer").is(":visible");
      }
    }

    var instance = {
      getSalaryOptions: function () {
        var options = [-800, -1000, -1500, -2000, -2500, -3000, -4000].map(function (val) {
          return {
            label: "Up to $" + Math.abs(val) + " per month",
            value: val
          };
        });
        options.push({
          label: "More than $4000 per month",
          value: 4000
        });
        return options
      },

      updateUserInfo: function () {
        $.each($rootScope.userInfo.profileNames, function (i, name) {
          // TODO: high-light provider icon
          $("a." + name.toLowerCase()).parent().addClass('active');
          $("a." + name.toLowerCase()).unbind("click");
        });
      },

      openOathDialog: function (auth) {
        utils.sendNotification(jsonValue.notifications.loading, $(window).height());
        $auth.authenticate(auth.provider)
          .then(function (resp) {//success
            delete $window.localStorage["satellizer_token"];
            $rootScope.userInfo.profileNames.push(auth.provider.toUpperCase());
            instance.updateUserInfo();
            //localStorageService.set(jsonValue.storage.key, resp.data.key);
            //$http.post("login", $.param({key: resp.data.key}), {headers:{'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}});
            //$location.path(jsonValue.routerUris.register);
          })
          .finally(function (resp) {
            utils.sendNotification(jsonValue.notifications.loaded);
          });
      }
    };

    //utils.registerNotification(jsonValue.notifications.userInfo, instance.updateUserInfo, $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });