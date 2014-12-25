angular.module('SignIn').factory('signInService',
  function (jsonValue, utils, shortcutFactory, $location, tourService, $http) {
    //var scope;

    var $$ = {
      enableNotifications: function () {
        return $(".signin-contianer").is(":visible");
      }
    }

    var instance = {
      initialize: function () {
        //scope = $scope;
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
        utils.openOathDialog(auth, jsonValue.routerUris.register);
      }
    };

    //utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });