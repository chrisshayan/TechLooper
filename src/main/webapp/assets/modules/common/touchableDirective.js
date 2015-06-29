techlooper
  .directive('touchable', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ngModelCtrl) {
        element.keypress(function () {
          ngModelCtrl.$edited = true;
        });

        if (attr.forcusout == 'true') {
          element.focusout(function () {
            ngModelCtrl.$edited = false;
          });
        }
      }
    }
  })
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
  });