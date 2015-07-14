techlooper.factory("resourcesService", function ($translate, $q) {
  var reviewStyleOptions = [{translate: "contestOwnerSignOff"}];
  var qualityIdeaOptions = [
    {translate: "hasAcceptableTradeoffs"},
    {translate: "theSolutionAchievesTheStatedGoals"},
    {translate: "theSolutionIsPracticalAndReliable"},
    {translate: "theSolutionIsInnovative"}
  ];

  var paymentOptions = [
    {translate: "fixedPricePayByProject", reviewTranslate: "fixedPrice", id: "fixedPrice"},
    {translate: "hourlyByByHour", reviewTranslate: "hourlyJob", id: "hourly"}
  ];

  var estimatedDurationOptions = [
    {translate: "more6m"},
    {translate: "3to6m"},
    {translate: "1to3m"},
    {translate: "lt1m"},
    {translate: "lt1w"}
  ];

  var estimatedWorkloadOptions = [
    {translate: "30+hrsw", id: "gt30"},
    {translate: "30-hrsw", id: "lt30"},
    {translate: "dontKnow", id: "dontKnow"}
  ];

  var singleSelectize = function (key) {
    return {
      create: false,
      valueField: 'title',
      labelField: 'title',
      maxItems: 1,
      plugins: ["techlooper"],
      getSelectize: function () {
        if (instance[key].selectizeDeffer) return instance[key].selectizeDeffer.promise;
        instance[key].selectizeDeffer = $q.defer();
        return instance[key].selectizeDeffer.promise;
      },
      onInitialize: function (selectize) {
        instance[key].selectizeDeffer.resolve(selectize);
      }
    }
  }

  var instance = {
    reviewStyleConfig: $.extend(true, {}, {options: reviewStyleOptions}, singleSelectize("reviewStyleConfig")),
    qualityIdeaConfig: $.extend(true, {}, {options: qualityIdeaOptions}, singleSelectize("qualityIdeaConfig")),
    paymentConfig: $.extend(true, {}, {options: paymentOptions}, singleSelectize("paymentConfig")),
    estimatedDurationConfig: $.extend(true, {}, {options: estimatedDurationOptions}, singleSelectize("estimatedDurationConfig")),
    estimatedWorkloadConfig: $.extend(true, {}, {options: estimatedWorkloadOptions}, singleSelectize("estimatedWorkloadConfig")),
    inOptions: function (title, config) {
      var index = -1;
      $.each(config.options, function (i, opt) {
        if (opt.title === title) {
          index = i;
          return false;
        }
      });
      return index;
    },
    getOption: function(title, config) {
      var option = undefined;
      $.each(config.options, function (i, opt) {
        if (opt.title === title) {
          option = opt;
          return false;
        }
      });
      return option;
    }
  }

  var translations = [
    {ins: instance.reviewStyleConfig, placeholder: "exContestOwnerSignOff"},
    {ins: instance.qualityIdeaConfig, placeholder: "exQualityIdeaConfig"},
    {ins: instance.paymentConfig, placeholder: "exPaymentConfig"},
    {ins: instance.estimatedDurationConfig, placeholder: "exEstimatedDurationConfig"},
    {ins: instance.estimatedWorkloadConfig, placeholder: "exEstimatedWorkloadConfig"}
  ];

  $.each(translations, function (i, item) {
    item.ins.getSelectize().then(function ($selectize) {
      $translate(item.placeholder).then(function (translate) {
        item.ins.placeholder = translate;
        $selectize.setPlaceholder(translate);
      });

      $.each(item.ins.options, function (i, row) {
        $translate(row.translate).then(function (translate) {
          row.title = translate;
        });
      });
    });
  });

  return instance;
});