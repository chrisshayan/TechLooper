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
      config: {
        valueField: 'value',
        labelField: 'value',
        delimiter: '|',
        searchField: ['value'],
        maxItems: 1,
        placeholder: $translate.instant("exYob")
      }
    },

    locationsSelectize: {
      items: jsonValue.locations.filter(function (location) {return location.id > 0; }),
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 1,
        searchField: ['name'],
        placeholder: $translate.instant("exHoChiMinh")
      }
    },

    companySizeSelectize: {
      items: jsonValue.companySizesArray,
      config: $.extend(true, translateConfigBase, {
        valueField: 'id',
        labelField: 'size',
        searchField: ['size'],
        placeholder: $translate.instant("ex149")
      })
    },

    industriesSelectize: {
      items: jsonValue.industriesArray,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name'],
        placeholder: $translate.instant("exItSoftware")
      }
    },

    timeToSendsSelectize: {
      items: $.extend(true, [], jsonValue.timeToSends),
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate'],
        placeholder: $translate.instant("exDay")
      }
    }
  }

  return instance;
});