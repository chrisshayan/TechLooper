techlooper.directive('confirmOnExit', function ($parse) {
  return {
    require: '^form',
    restrict: 'A',
    scope: true,
    link: function (scope, element, attrs, ngModelCtrl) {
      console.log(scope, element, attrs, ngModelCtrl);
      window.onbeforeunload = function () {
        if (ngModelCtrl.$dirty) {
          return "The form is dirty, do you want to stay on the page?";
        }
      }
      scope.$on('$locationChangeStart', function (event, next, current) {
        if (ngModelCtrl.$dirty) {
          if (!confirm("The form is dirty, do you want to stay on the page?")) {
            event.preventDefault();
          }
        }
      });
    }
  }
});