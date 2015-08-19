techlooper.directive('datetimePicker', function ($timeout) {
  return {
    restrict: 'A',
    require: "ngModel",
    scope: {
      options: '='
    },
    link: function (scope, element) {
      var options = scope.options || {};


      var init_options = {
        format: "d/m/Y h:i A",
        allowTimes: [],
        step: 30,
        onChangeDateTime: function (new_date, input) {
          scope.$apply(function () {
            scope.ngModel = new_date;
          });
        }
      }

      $.extend(options, init_options);

      $(element[0]).datetimepicker(options);
    }
  }
});