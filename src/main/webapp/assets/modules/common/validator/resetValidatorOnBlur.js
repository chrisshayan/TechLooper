techlooper.directive("resetValidatorOnBlur", function(){
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, element, attrs, ctrl){
      ctrl.$parsers.unshift(function(value) {
        var validators = attrs.resetValidatorOnBlur.split(",");
        _.each(validators, function(validator) {
          ctrl.$setValidity(validator, true);
        });
        return value;
      });
    }
  }
});