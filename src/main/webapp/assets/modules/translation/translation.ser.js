angular.module('Common').factory('translationService', function ($translate, utils, jsonValue) {
    var scope;
    var instance = {
        initialize: function ($scope) {
            scope = $scope;
            scope.changeLanguage = function () {
                var lang = instance.getNextLanguage();
                $translate.use(lang).then(function () {utils.sendNotification(jsonValue.notifications.changeLang, lang);});
                scope.icoLanguage = instance.getNextLanguage(lang);
            };
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