techlooper.directive('inputNumber', function ($parse) {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function (scope, element, attrs, ngModelCtrl) {
      ngModelCtrl.$parsers.push(function(val) {
        if (!val) return "";
        var digits = val.replace(/[^0-9]/g, '');
        var number = "";
        if (digits !== val) {
          if (attrs.inputNumberFraction) {
            var parts = val.split(".");
            if (parts.length > 2) {
              var part0 = parts[0].replace(/[^0-9]/g, '');
              var part1 = parts[1].substring(0, parseInt(attrs.inputNumberFraction)).replace(/[^0-9]/g, '');
              number = part0 + "." + part1;
              ngModelCtrl.$setViewValue(number);
              ngModelCtrl.$render();
            }
            else if (parts.length === 2) {
              var part0 = parts[0].replace(/[^0-9]/g, '');
              var part1 = parts[1].substring(0, parseInt(attrs.inputNumberFraction)).replace(/[^0-9]/g, '');
              number = part0 + "." + part1;
              if (number !== val) {
                ngModelCtrl.$setViewValue(number);
                ngModelCtrl.$render();
              }
            }
            else {
              ngModelCtrl.$setViewValue(digits);
              ngModelCtrl.$render();
            }
          }
          else {
            ngModelCtrl.$setViewValue(digits);
            ngModelCtrl.$render();
          }
        }

        number = number.length > 0 ? parseFloat(number) : parseInt(digits);
        if (!isNaN(number)) {
          return number;
        }
        return "";
      });
    }
  };
});