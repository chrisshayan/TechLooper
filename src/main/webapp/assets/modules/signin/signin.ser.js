angular.module('SignIn').factory('signInService', function (jsonValue, utils, shortcutFactory, $location, tourService, $auth) {
  var scope;

  //var clientIds = {
  //  linkedin: "75ukeuo2zr5y3n"
  //}
  //hello.init(clientIds);
  //hello.on('auth.login',  function (auth) {
  //  console.log(123, auth);
  //});

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
      $auth.authenticate(auth.provider).then(function(response) {
        $location.path(jsonValue.routerUris.register);
      });
      //if (!clientIds.hasOwnProperty(auth.site)) {//no support login
      //  return;
      //}
      //
      //var opts = $.extend({redirect_uri: 'authentication.html'}, auth.oauthOptions);
      //hello(auth.site).login(opts);
    }
  };

  utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize, $$.enableNotifications);
  return instance;
});