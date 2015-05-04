techlooper
  .directive("offerLetter", function ( utils, jsonValue) {
    return {
      restrict: "A",
      replace: true,
      templateUrl: function(elem, attrs) {
        switch (utils.getView()) {
          case jsonValue.views.salarySharing:
            return "modules/offer-letter/salary-sharing.tem.html";
          case jsonValue.views.salaryReport:
            return "modules/offer-letter/salary-report.tem.html";
        }
        return '';
      }
    }
  });