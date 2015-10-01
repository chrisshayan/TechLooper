techlooper.factory('languageHttpInterceptor', function ($rootScope, jsonValue, $q, $translate) {
  var instance = {
    request: function (config) {
      config.params = config.params || {};
      config.params.lang = $translate.use();
      return config || $q.when(config);
    }
  };

  return instance;
});