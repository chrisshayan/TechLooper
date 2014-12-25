angular.module('SignIn').factory('signInService',
  function (jsonValue, utils, shortcutFactory, $location, tourService, $auth, localStorageService, $window, $http) {
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
        if (auth.isNotSupported) {return alert("Sign-in by " + auth.provider.toUpperCase() + " isn't supported");}
        utils.sendNotification(jsonValue.notifications.loading, $(".signin-page").height());
        $auth.authenticate(auth.provider)
          .then(function (resp) {//success
            delete $window.localStorage["satellizer_token"];
            localStorageService.set(jsonValue.storage.key, resp.data.key);
            $http.post("login", $.param({key: resp.data.key}), {headers:{'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}})
              .success(function() {
                $location.path(jsonValue.routerUris.register);
              })
              .error(function() {
                //TODO: invalid user credential
                utils.sendNotification(jsonValue.notifications.loaded);
              });
          })
          .catch(function(resp) {
            utils.sendNotification(jsonValue.notifications.loaded);
          });
      }
    };

    //utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
    return instance;
  });