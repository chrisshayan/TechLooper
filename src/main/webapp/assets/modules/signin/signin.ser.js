angular.module('SignIn').factory('signInService',
  function (jsonValue, utils, shortcutFactory, $location, tourService, $auth) {
    var scope;

    var $$ = {
      initialize: function ($scope) {
        scope = $scope;
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

      enableNotifications: function () {
        return $(".signin-page").is(":visible");
      }
    }

    var instance = {
      openOathDialog: function (auth) {
        $auth.authenticate(auth.provider).then(function (response) {
          $location.path(jsonValue.routerUris.register);
        });
      }
    };

    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });