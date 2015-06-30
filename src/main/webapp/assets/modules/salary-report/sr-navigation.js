techlooper.directive("srNavigation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/salary-report/sr-navigation.tem.html"
  }
}).directive("srcollWindow", function ($window) {
  return {
    scope: {
      scroll: '=srcollWindow'
    },
    link: function(scope, element, attrs) {
      var windowEl = angular.element($window);
      var handler = function() {
        scope.scroll = windowEl.scrollTop();
      }
      windowEl.on('scroll', scope.$apply.bind(scope, handler));
      handler();
    }
  }
});