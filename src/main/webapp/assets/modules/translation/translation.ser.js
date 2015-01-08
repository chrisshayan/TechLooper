angular.module('Common').factory('translationService', function ($translate, utils, jsonValue) {
  var scope;

  var instance = {
    initialize: function ($scope) {
      scope = $scope;
      $(".langKey").on('click', function () {
        var nextLang = instance.getNextLanguage();
        $translate.use(nextLang);
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
