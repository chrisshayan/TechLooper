techlooper.directive('confirmOnExit', function ($rootScope) {
  return {
    require: '^form',
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      //window.onbeforeunload = function () {
      //  if (ngModelCtrl.$dirty) {
      //    return "You are about to lose your unsaved changes. Are you sure you want to leave this page?";
      //  }
      //}
      //scope.$on('$routeChangeStart', function (event, next, current) {
      //  console.log(123);
      //  console.log(event.currentScope.$$phase);
      //  //if (ngModelCtrl.$dirty) {
      //  //  if(!event.currentScope.$$phase) {
      //  //    return;
      //  //  }
      //  //    if (!confirm("You are about to lose your unsaved changes. Are you sure you want to leave this page?")) {
      //  //      event.preventDefault();
      //  //    }
      //  //}
      //
      //  return false;
      //});
    }
  }
});