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
        //scope.$watch(attrs.ngModel,
        //  function (valid, oldValue) {
        //    console.log($parse(attrs.requiredExpr)(scope));
        //    ngModelCtrl.$setValidity("requiredExpr", $parse(attrs.requiredExpr)(scope));
        //  });
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