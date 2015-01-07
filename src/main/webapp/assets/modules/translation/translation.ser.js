angular.module('Common').factory('translationService', function ($translate, utils, jsonValue) {
  var scope;

  var instance = {
    initialize: function ($scope) {
      scope = $scope;
      $(".langKey").click(function () {
        var nextLang = instance.getNextLanguage();
        $translate.use(nextLang).then(function () {utils.sendNotification(jsonValue.notifications.changeLang, lang);});
        var lang = nextLang;
        utils.apply();

        nextLang = instance.getNextLanguage(nextLang);
        scope.icoLanguage = nextLang;
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
          return "vi";
      }
    }
  }

  return instance;
});
