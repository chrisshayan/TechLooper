angular.module('Common').factory('translationService', function ($translate, utils, jsonValue) {
  var scope;

  var instance = {
    initialize: function ($scope) {
      scope = $scope;
      $(".langKey").click(function () {
        var nextLang = instance.getNextLanguage();
        $translate.use(nextLang);
        utils.apply();

        nextLang = instance.getNextLanguage(nextLang);
        scope.icoLanguage = nextLang;
        utils.sendNotification(jsonValue.notifications.changeLang);
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
