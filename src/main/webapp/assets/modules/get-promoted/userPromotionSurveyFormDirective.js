techlooper.directive("userPromotionSurveyForm", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/userPromotionSurveyForm.html",
    link: function (scope, element, attr, ctrl) {
      scope.showError = function(form, prop) {
        var submitted = form[prop].$touched || form.$submitted;
      }
    }
  }
});