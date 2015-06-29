techlooper
  .directive('touchable', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ngModelCtrl) {
        element.keypress(function () {
          ngModelCtrl.$setTouched();
        });
        element.focusout(function () {
          ngModelCtrl.$setUntouched();
        });
      }
    }
  });