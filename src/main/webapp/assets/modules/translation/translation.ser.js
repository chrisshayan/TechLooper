angular.module('Common').factory('translationService', function ($translate, utils, jsonValue) {
  var scope;

  var instance = {
    initialize: function ($scope) {
      scope = $scope;
      $(".langKey").click(function () {
        var lang = instance.getNextLanguage();
        $translate.use(lang).then(function () {utils.sendNotification(jsonValue.notifications.changeLang, lang);});
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
