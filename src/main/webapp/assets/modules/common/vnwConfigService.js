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
      config: $.extend(true, {}, translateConfigBase)
    },

    gendersSelectize: {
      items: jsonValue.genders,
      config: $.extend(true, {}, translateConfigBase)
    },

    yobsSelectize: {
      items: jsonValue.yobs,
      config: {
        valueField: 'value',
        labelField: 'value',
        delimiter: '|',
        searchField: ['value'],
        maxItems: 1
      }
    },

    locationsSelectize: {
      items: jsonValue.locations.filter(function (location) {return location.id > 0; }),
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 1,
        searchField: ['name']
      }
    },

    companySizeSelectize: {
      items: jsonValue.companySizesArray,
      config: $.extend(true, translateConfigBase, {
        valueField: 'id',
        labelField: 'size',
        searchField: ['size']
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
        searchField: ['name']
      }
    },

    timeToSendsSelectize: {
      items: $.extend(true, [], jsonValue.timeToSends),
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate']
      }
    }
  }

  var doTranslate = function(i, item) {
    $translate(item.translate).then(function(translate) {item.translate = translate;});
  }

  $.each(instance.jobLevelsSelectize.items, doTranslate);
  $.each(instance.gendersSelectize.items, doTranslate);
  $.each(instance.timeToSendsSelectize.items, doTranslate);

  $translate(["exManager", "exMale", "exYob", "exHoChiMinh", "ex149", "exItSoftware", "exDay"]).then(function(trans) {
    instance.jobLevelsSelectize.config.placeholder = trans.exManager;
    instance.gendersSelectize.config.placeholder = trans.exMale;
    instance.yobsSelectize.config.placeholder = trans.exYob;
    instance.locationsSelectize.config.placeholder = trans.exHoChiMinh;
    instance.companySizeSelectize.config.placeholder = trans.ex149;
    instance.industriesSelectize.config.placeholder = trans.exItSoftware;
    instance.timeToSendsSelectize.config.placeholder = trans.exDay;
  });
  return instance;
});