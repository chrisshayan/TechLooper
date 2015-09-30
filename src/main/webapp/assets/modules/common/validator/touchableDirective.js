techlooper
  .directive('touchable', function ($rootScope) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ngModelCtrl) {
        element.keypress(function () {
          ngModelCtrl.$edited = true;
        });

        if (attr.onchange == 'true') {
          scope.$watch(attr.ngModel, function(newVal, oldVal) {
            if (!newVal && !oldVal) return;
            ngModelCtrl.$edited = true;
          });
        }

        if (attr.focusout == 'true') {
          element.focusout(function () {
            ngModelCtrl.$edited = false;
          });
          element.find("input").focusout(function () {
            ngModelCtrl.$edited = false;
          });
        }

        $rootScope.$on("$setPristine", function() {
          ngModelCtrl.$edited = false;
        });
      }
    }
  });