techlooper
  .directive('touchable', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ngModelCtrl) {
        element.keypress(function () {
          ngModelCtrl.$edited = true;
          console.log(ngModelCtrl);
        });

        if (attr.forcusout == 'true') {
          element.focusout(function () {
            ngModelCtrl.$edited = false;
          });
        }
      }
    }
  });