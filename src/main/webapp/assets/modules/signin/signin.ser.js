angular.module('SignIn').factory('signInService',
  function (jsonValue, utils, shortcutFactory, $location, tourService, $auth, localStorageService,
            $window, $http, connectionFactory, $rootScope, userService) {

    var $$ = {
      enableNotifications: function () {
        return utils.getView() === jsonValue.views.signIn;
      },

      loginFailed: function () {
        // TODO: consider to use a "signing box"
        utils.sendNotification(jsonValue.notifications.hideLoadingBox);
      }
    }

    var instance = {
      init: function () {},

      initialize: function () {
        utils.sendNotification(jsonValue.notifications.loading, $(window).height());

        // check if user already login
        if (userService.verifyUserSession()) {
          connectionFactory.verifyUserLogin()
            .then(function () {utils.sendNotification(jsonValue.notifications.loginSuccess);})
            .catch(function () {utils.sendNotification(jsonValue.notifications.loginFailed);});
        }
        else {
          utils.sendNotification(jsonValue.notifications.hideLoadingBox);
        }

        $('.signin-accounts').parallax();

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

        $(".signin-popup-close").on('click', function () {
          $('#signin-form').modal('hide');
        });

        tourService.makeTourGuide();
      },

      openOathDialog: function (auth) {
        if (auth.isNotSupported) {return alert("Sign-in by " + auth.provider.toUpperCase() + " isn't supported");}
        utils.sendNotification(jsonValue.notifications.loading, $(window).height());
        $auth.authenticate(auth.provider)
          .then(function (resp) {//success
            delete $window.localStorage["satellizer_token"];
            localStorageService.cookie.set(jsonValue.storage.key, resp.data.key);
            connectionFactory.login();
          })
          .catch(function (resp) {
            utils.sendNotification(jsonValue.notifications.loaded);
          });
      }
    };

    //utils.registerNotification(jsonValue.notifications.loginSuccess, $$.loginSuccess);
    utils.registerNotification(jsonValue.notifications.loginFailed, $$.loginFailed);
    return instance;
  });