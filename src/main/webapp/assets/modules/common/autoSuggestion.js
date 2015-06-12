techlooper.directive('autoSuggestion', function ($http) {
  return {
    restrict: "A",
    scope: {
      text: "=",
      items: "=",
      getUrl: "@"
    },
    link: function (scope, element, attr, ctrl) {
      scope.$watch("text", function (newVal) {
        if (!newVal) return;
        delete scope.items;
        $http.get(scope.getUrl + scope.text)
          .success(function (data) {
            scope.items = data.items.map(function (item) {return item.name;});
          });
      });
    }
  }
});