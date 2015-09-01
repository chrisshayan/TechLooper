techlooper.factory("resourcesService", function ($translate, $q) {
  var reviewStyleOptions = [
    {translate: "contestOwnerSignOff", id: "contestOwnerSignOff"}
  ];

  var qualityIdeaOptions = [
    {translate: "hasAcceptableTradeoffs", id: "hasAcceptableTradeoffs"},
    {translate: "theSolutionAchievesTheStatedGoals", id: "theSolutionAchievesTheStatedGoals"},
    {translate: "theSolutionIsPracticalAndReliable", id: "theSolutionIsPracticalAndReliable"},
    {translate: "theSolutionIsInnovative", id: "theSolutionIsInnovative"}
  ];

  var paymentOptions = [
    {translate: "hourlyByByHour", reviewTranslate: "hourlyJob", id: "hourly"},
    {translate: "fixedPricePayByProject", reviewTranslate: "fixedPrice", id: "fixedPrice"}
  ];

  var estimatedDurationOptions = [
    {translate: "more6m", id: "more6m"},
    {translate: "3to6m", id: "3to6m"},
    {translate: "1to3m", id: "1to3m"},
    {translate: "lt1m", id: "lt1m"},
    {translate: "lt1w", id: "lt1w"}
  ];

  var estimatedWorkloadOptions = [
    {translate: "gt30hrsw", id: "gt30hrsw"},
    {translate: "lt30hrsw", id: "lt30hrsw"},
    {translate: "dontKnow", id: "dontKnow"}
  ];

  var titleSelectize = function (key) {
    return {
      create: false,
      valueField: 'title',
      labelField: 'title',
      maxItems: 1,
      plugins: ["techlooper"],
      selectizeDeffer: $q.defer(),
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

  var idSelectize = function (key) {
    return {
      create: false,
      valueField: 'id',
      labelField: 'title',
      maxItems: 1,
      plugins: ["techlooper"],
      selectizeDeffer: $q.defer(),
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
    reviewStyleConfig: $.extend(true, {}, {options: reviewStyleOptions}, titleSelectize("reviewStyleConfig")),
    qualityIdeaConfig: $.extend(true, {}, {options: qualityIdeaOptions}, titleSelectize("qualityIdeaConfig")),
    paymentConfig: $.extend(true, {}, {options: paymentOptions}, idSelectize("paymentConfig")),
    estimatedDurationConfig: $.extend(true, {}, {options: estimatedDurationOptions}, idSelectize("estimatedDurationConfig")),
    estimatedWorkloadConfig: $.extend(true, {}, {options: estimatedWorkloadOptions}, idSelectize("estimatedWorkloadConfig")),
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
    getOption: function(id, config) {
      var option = undefined;
      $.each(config.options, function (i, opt) {
        if (opt.id === id) {
          option = opt;
          return false;
        }
      });
      return option;
    }

    //initialize: function() {}
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
        //console.log(row);
        //console.log($.type(row));
        //console.log(row);
        if ($.type(row) == "array") {
          //row = row[0];
          //console.log(row);
          item.ins.options = [];
          $.each(row, function (i, r) {
            $translate(r.translate).then(function (translate) {
              r.title = translate;
              item.ins.options.push(r);
            });
          });
          //console.log(row);
          //console.log(array);
          //
          //return (item.ins.options = array);
          return ;
        }
        $translate(row.translate).then(function (translate) {
          row.title = translate;
        });
      });
    });
  });

  return instance;
});