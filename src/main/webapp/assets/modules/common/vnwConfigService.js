techlooper.factory("vnwConfigService", function (jsonValue, $translate, $rootScope, utils) {

  var translateConfigBase = {
    valueField: 'id',
    labelField: 'translate',
    delimiter: '|',
    maxItems: 1,
    searchField: ['translate']
  }

  //TODO 1. Translation, 2. Validation

  var instance = {
    jobLevelsSelectize: {
      items: $.extend(true, [], jsonValue.jobLevels.filter(function (jobLevel) {return jobLevel.id > 0;})),
      config: $.extend(true, {placeholder: $translate.instant("exManager")}, translateConfigBase)
    },

    gendersSelectize: {
      items: jsonValue.genders,
      config: $.extend(true, {placeholder: $translate.instant("exMale")}, translateConfigBase)
    },

    yobsSelectize: {
      items: jsonValue.yobs,
      config: $.extend(true, {placeholder: $translate.instant("exYob")}, translateConfigBase)
    }
  }
  return instance;
});