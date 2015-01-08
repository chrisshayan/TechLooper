angular.module('Common').factory('translationService', function ($translate, utils, jsonValue) {
  var scope;

  var instance = {
    initialize: function ($scope) {
      scope = $scope;
<<<<<<< HEAD
      $(".langKey").on('click', function () {
        var nextLang = instance.getNextLanguage();
        $translate.use(nextLang);
=======
      $(".langKey").click(function () {
        var lang = instance.getNextLanguage();
        $translate.use(lang).then(function () {utils.sendNotification(jsonValue.notifications.changeLang, lang);});
>>>>>>> 56adb09f4f6fd3d0cd757892496c5f28dc1e1e8a
        utils.apply();

        var nextLang = instance.getNextLanguage(lang);
        scope.icoLanguage = nextLang;
        scope.$apply();
      });
    },

    getNextLanguage: function (lang) {
      var language = lang === undefined ? $translate.use() : lang;
      switch (language) {
        case "vi":
          return "en";
        case "en":
          return "vi";
        default:
          return "en";
      }
    }
  }

  return instance;
});
