techlooper
  .directive('arrayModel', function ($parse) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attrs, ngModelCtrl) {
        scope.$watch(attrs.ngModel, function (newValue, oldValue) {
          var array = $parse(attrs.ngModel)(scope);
          ngModelCtrl.$setValidity("required", array.length > 0);
        }, true);
      }
    }
  });