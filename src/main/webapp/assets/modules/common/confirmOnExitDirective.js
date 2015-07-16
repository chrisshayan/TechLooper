techlooper.directive('confirmOnExit', function ($rootScope, $confirm, $window, $location) {
  return {
    require: '^form',
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      window.onbeforeunload = function () {
        if (ngModelCtrl.$dirty) {
          return "The form is dirty, do you want to stay on the page?";
        }
      }

      var processing = false;
      var confirmed = false;
      var e = undefined;
      scope.$on('$locationChangeStart', function (event, next, current) {
        if (processing) {
          console.log(confirmed);
          //if (!e) {
          return event.preventDefault();
          //}
        }
        if (ngModelCtrl.$dirty) {
          processing = true;
          confirmed = confirm("You have unsaved edits. Do you wish to leave?");
          //confirmed = true;
          ////e = event;
          if (!confirmed) {
            //  //processing = false;
            event.preventDefault();
          }

          //$confirm({text: 'Are you sure you want to delete?'})
          //  .then(function() {
          //    scope.deletedConfirm = 'Deleted';
          //  });
          //
          //event.preventDefault();
          //else {
          processing = false;
          //return true;
          //}
        }
      });
    }
  }
});