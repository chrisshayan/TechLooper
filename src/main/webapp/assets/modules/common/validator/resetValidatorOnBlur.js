techlooper.directive("resetValidatorOnBlur", function(){
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, element, attrs, ctrl){
      ctrl.$parsers.unshift(function(value) {
        ctrl.$setValidity(attrs.resetValidatorOnBlur, true);
        return value;
      });
    }
  }
});