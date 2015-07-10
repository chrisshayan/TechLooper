techlooper.factory("resourcesService", function ($translate, $q) {
  var reviewStyleOptions = [{translate: "contestOwnerSignOff"}];
  var qualityIdeaOptions = [
    {translate: "hasAcceptableTradeoffs"},
    {translate: "theSolutionAchievesTheStatedGoals"},
    {translate: "theSolutionIsPracticalAndReliable"},
    {translate: "theSolutionIsInnovative"}
  ];

  var paymentOptions = [
    {translate: "fixedPrice"},
    {translate: "hourly"}
  ];

  var singleSelectize = function (key) {
    return {
      create: false,
      valueField: 'title',
      labelField: 'title',
      maxItems: 1,
      plugins: ["techlooper"],
      getSelectize: function () {
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
    paymentConfig:  $.extend(true, {}, {options: paymentOptions}, singleSelectize("paymentConfig"))
  }

  var translations = [
    {ins: instance.reviewStyleConfig, placeholder: "exContestOwnerSignOff"},
    {ins: instance.qualityIdeaConfig, placeholder: "exQualityIdeaConfig"},
    {ins: instance.paymentConfig, placeholder: "exPaymentConfig"}
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