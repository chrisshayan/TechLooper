techlooper.directive("srSimilarReport", function (jsonValue, connectionFactory, $timeout, $translate, validatorService, $http) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-similar-report.tem.html",
    link: function () {
    }
  }
});