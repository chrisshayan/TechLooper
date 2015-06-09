techlooper.factory("validatorService", function ($translate) {

  var instance = {
    validate: function (elems, $error) {
      var error = $error || {};
      $.each(elems, function (i, elem) {
        if ($(elem).hasClass("ng-invalid-required")) {
          error[$(elem).attr("ng-model")] = $translate.instant('requiredThisField');
        }
        else if ($(elem).hasClass("ng-invalid-email")) {
          error[$(elem).attr("ng-model")] = $translate.instant('emailInvalid');
        }
      });
      return error;
    },

    validateElem: function (elem, val, $error) {
      var error = $error || {};
      if ($(elem).attr("required") && (!val || val.length === 0)) {
        error[$(elem).attr("ng-model")] = $translate.instant('requiredthisfield');
      }
      else {
        delete error[$(elem).attr("ng-model")];
      }
      return error;
    }
  };

  return instance;
});