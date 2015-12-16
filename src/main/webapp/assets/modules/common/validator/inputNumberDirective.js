techlooper.directive('inputNumber', function ($parse) {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      ngModelCtrl.$parsers.push(function (inputValue) {
        if (attrs.inputNumber == "strict" && !inputValue) inputValue = "0i";
        if (!inputValue) return "";

        var digits = inputValue.replace(/[^0-9]/g, '');
        var viewValue = digits;
        if (digits !== inputValue) {
          if (attrs.inputNumberFraction) {
            var parts = inputValue.split(".");
            if (parts.length > 1) {
              var part0 = parts[0].replace(/[^0-9]/g, '');
              var part1 = parts[1].substring(0, parseInt(attrs.inputNumberFraction)).replace(/[^0-9]/g, '');
              viewValue = part0 + "." + part1;
            }
          }
          ngModelCtrl.$setViewValue(viewValue);
          ngModelCtrl.$render();
        }

        if ($.isNumeric(viewValue)) {
          return viewValue;
        }
        return "";
      });
    }
  };
});