angular.module('SignIn').factory('signInService',
  function (jsonValue, utils, shortcutFactory, $location, tourService, $auth, localStorageService,
            $window, $http, connectionFactory, historyFactory) {
    //var scope;

    var $$ = {
      enableNotifications: function () {
        return $(".signin-contianer").is(":visible");
      },

      loginFailed: function () {
        // TODO: consider to use a "signing box"
        utils.sendNotification(jsonValue.notifications.hideLoadingBox);
      }
    }

    var instance = {
      init: function () {},

      initialize: function () {
        //scope = $scope;
        utils.sendNotification(jsonValue.notifications.loading);

        // check if user already login
        connectionFactory.login();

        $('.signin-accounts').parallax();

        $('.btn-close').click(function () {
          shortcutFactory.trigger('esc');
        });

        $('.btn-logo').click(function () {
          shortcutFactory.trigger('esc');
        });

        $(".signin-popup-close").on('click', function () {
          $('#signin-form').modal('hide');
        });

        tourService.makeTourGuide();
      },

      openOathDialog: function (auth) {
        if (auth.isNotSupported) {return alert("Sign-in by " + auth.provider.toUpperCase() + " isn't supported");}
        utils.sendNotification(jsonValue.notifications.loading, $(".signin-page").height());
        $auth.authenticate(auth.provider)
          .then(function (resp) {//success
            delete $window.localStorage["satellizer_token"];
            localStorageService.set(jsonValue.storage.key, resp.data.key);
            connectionFactory.login();
          })
          .catch(function (resp) {
            utils.sendNotification(jsonValue.notifications.loaded);
          });
      }
    };

    //utils.registerNotification(jsonValue.notifications.loginSuccess, $$.loginSuccess,  $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.loginFailed, $$.loginFailed);
    return instance;
  });