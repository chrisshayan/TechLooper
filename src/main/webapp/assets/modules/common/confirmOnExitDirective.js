techlooper.directive('confirmOnExit', function ($rootScope) {
  return {
    require: '^form',
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      //window.onbeforeunload = function () {
      //  if (ngModelCtrl.$dirty) {
      //    return "The form is dirty, do you want to stay on the page?";
      //  }
      //}
      //scope.$on('$routeChangeStart', function (event, next, current) {
      //  console.log(123);
      //  console.log(event.currentScope.$$phase);
      //  //if (ngModelCtrl.$dirty) {
      //  //  if(!event.currentScope.$$phase) {
      //  //    return;
      //  //  }
      //  //    if (!confirm("The form is dirty, do you want to stay on the page?")) {
      //  //      event.preventDefault();
      //  //    }
      //  //}
      //
      //  return false;
      //});
    }
  }
});