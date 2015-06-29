techlooper
  .directive('requiredExpr', function ($parse) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attrs, ngModelCtrl) {
        scope
          .$watch(function () {
            return $parse(attrs.requiredExpr)(scope);
          },
          function (valid, oldValue) {
            ngModelCtrl.$setValidity("requiredExpr", valid);
          });
      }
    }
  })
  .directive('requiredExpr2', function ($parse) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attrs, ngModelCtrl) {
        scope
          .$watch(function () {
            return $parse(attrs.requiredExpr2)(scope);
          },
          function (valid, oldValue) {
            ngModelCtrl.$setValidity("requiredExpr2", valid);
          });
      }
    }
  });