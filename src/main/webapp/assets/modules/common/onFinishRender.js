techlooper.directive("onFinishRender", function ($timeout, jsonValue, utils) {
  return {
    restrict: "A",
    link: function (scope, element, attr) {
      if (scope.$last === true) {
        scope.$emit(attr.onFinishRender);
      }
    }
  }
});