techlooper
  .directive('touchable', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ngModelCtrl) {
        element.keypress(function () {
          //ngModelCtrl.$setTouched();
          ngModelCtrl.$edited = true;
        });

        //element.focusout(function () {
        //  ngModelCtrl.$setUntouched();
        //});

        if (attr.forcusout == 'true') {
          element.focusout(function () {
            ngModelCtrl.$edited = false;
          });
        }
      }
    }
  });