techlooper.factory("resourcesService", function ($translate, $q) {
  var reviewStyleOptions = [{translate: "contestOwnerSignOff"}];

  var singleSelectize = function(key) {
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
    reviewStyleConfig: $.extend(true, {}, {options: reviewStyleOptions}, singleSelectize("reviewStyleConfig"))
  }

  var translations = [
    {ins: instance.reviewStyleConfig, placeholder: "exContestOwnerSignOff"},
  ];

  $.each(translations, function (i, item) {

    item.ins.getSelectize().then(function ($selectize) {
      $translate(item.placeholder).then(function (translate) {
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