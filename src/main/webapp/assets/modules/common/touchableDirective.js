techlooper
  .directive('touchable', function ($rootScope) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ngModelCtrl) {
        element.keypress(function () {
          ngModelCtrl.$edited = true;
        });


        if (attr.focusout == 'true') {
          element.focusout(function () {
            ngModelCtrl.$edited = false;
          });
        }
      }
    }
  });