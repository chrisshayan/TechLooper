techlooper.directive("onFinishRender", function ($timeout, jsonValue, utils) {
  return {
    restrict: "A",
    link: function (scope, element, attr) {
      if (scope.$last === true) {
        $timeout(function () {
          scope.$emit(attr.onFinishRender);
        });
        utils.sendNotification(jsonValue.notifications.loaded, $(window).height());
      }
    }
  }
});