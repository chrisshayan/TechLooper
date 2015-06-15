techlooper.directive('touchable', function () {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function (scope, element, attr, ngModelCtrl) {
      scope.$watch(attr.ngModel, function (newVal, oldVal) {
        if (ngModelCtrl.$pristine) return;
        if (!newVal && !oldVal) {
          return false;
        }
        ngModelCtrl.$touched = true;
      });
    }
  }
});