techlooper.directive("navigation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/navigation/navigation.tem.html",
    controller: "navigationController"
  }
})
  .directive("findjobs", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/job/findJobs.tem.html"
    }
  })
  .directive('onlyDigits', function ($filter) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ctrl) {
        function inputValue(val) {
          if (val) {
            var digits = val.replace(/[^0-9.]/g, '');
            if (digits !== val) {
              ctrl.$setViewValue(digits);
              ctrl.$render();
            }
            var number = parseFloat(digits);
            if (!isNaN(number)) {
              //number = $filter('number')(number, 2);
              //ctrl.$setViewValue(number);
              //ctrl.$render();
              return number;
            }
            return "";
          }
          return '';
        }

        ctrl.$parsers.push(inputValue);
      }
    }
  })
  .directive('onlyDigitsString', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function (scope, element, attr, ctrl) {
        function inputValue(val) {
          if (val) {
            var digits = val.replace(/[^0-9.]/g, '');

            if (digits !== val) {
              ctrl.$setViewValue(digits);
              ctrl.$render();
            }
            var number = parseFloat(digits);
            return isNaN(number) ? "" : val;

          }
          return '';
        }

        ctrl.$parsers.push(inputValue);
      }
    }
  });
