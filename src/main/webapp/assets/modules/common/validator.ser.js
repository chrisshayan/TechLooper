angular.module("Common").factory("validatorService", function ($translate) {

  var instance = {
    validate: function(elems) {
      var error = {};
      $.each(elems, function(i, elem) {
        if ($(elem).hasClass("ng-invalid-required")) {
          error[$(elem).attr("ng-model")] = $translate.instant('requiredThisField');
        }
        else if ($(elem).hasClass("ng-invalid-email")) {
          error[$(elem).attr("ng-model")] = $translate.instant('emailInvalid');
        }

        //if (!error[$(elem).attr("ng-model")]) {
        //  $(elem).has("[ng-model]:has[ng-required]")
        //
        //}
      });
      return  error;
    }
  };

  return instance;
});