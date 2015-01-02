angular.module('SignIn').factory('signInService',
  function (jsonValue, utils, shortcutFactory, $location, tourService, $auth, localStorageService, $window, $http, connectionFactory) {
    //var scope;

    var $$ = {
      enableNotifications: function () {
        return $(".signin-contianer").is(":visible");
      },

      loginFailed: function() {
        localStorageService.clearAll();
        // TODO: consider to use a "signing box"
        utils.sendNotification(jsonValue.notifications.hideLoadingBox);
      }
    }

    var instance = {
      initialize: function (register) {
        if (register) {
          return undefined;
        }
        //scope = $scope;
        utils.sendNotification(jsonValue.notifications.loading);
        connectionFactory.login(function() {
          $location.path("/");
        });

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

        //$('.sign-successful').on('click', function () {
        //  $location.path(jsonValue.routerUris.register);
        //  $scope.$apply();
        //});

        tourService.makeTourGuide();
      },

      openOathDialog: function (auth) {
        if (auth.isNotSupported) {return alert("Sign-in by " + auth.provider.toUpperCase() + " isn't supported");}
        utils.sendNotification(jsonValue.notifications.loading, $(".signin-page").height());
        $auth.authenticate(auth.provider)
          .then(function (resp) {//success
            delete $window.localStorage["satellizer_token"];
            localStorageService.set(jsonValue.storage.key, resp.data.key);
            connectionFactory.login(function() {
              $location.path(jsonValue.routerUris.register);
            });
          })
          .catch(function(resp) {
            utils.sendNotification(jsonValue.notifications.loaded);
          });
      }
    };

    //utils.registerNotification(jsonValue.notifications.loginSuccess, $$.loginSuccess,  $$.enableNotifications);
    utils.registerNotification(jsonValue.notifications.loginFailed, $$.loginFailed);
    return instance;
  });