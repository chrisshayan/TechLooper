angular.module("Common").factory("validatorService", function ($translate) {

  var instance = {
    validate: function(elems) {
      var error = {};
      $.each(elems, function(i, elem) {
        $(elem).hasClass("ng-invalid-required") && (error[$(elem).attr("ng-model")] = $translate.instant('requiredThisField'));
        $(elem).hasClass("ng-invalid-email") && (error[$(elem).attr("ng-model")] = $translate.instant('emailInvalid'));
      });
      return  error;
    }
  };

  return instance;
});