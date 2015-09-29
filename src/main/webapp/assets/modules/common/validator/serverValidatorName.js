techlooper.directive("serverValidatorName", function(){
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, element, attrs, ctrl){
      //ctrl.$parsers.unshift(function(value) {
      //  ctrl.$setValidity(attrs.serverValidatorName, true);
      //  return value;
      //});
    }
  }
});